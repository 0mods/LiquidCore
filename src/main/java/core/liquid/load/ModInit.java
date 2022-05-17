package core.liquid.load;

import core.liquid.objects.annotations.Register;
import core.liquid.registry.api.LiquidRegister;
import core.liquid.registry.base.LiquidRegistry;
import core.liquid.registry.base.tasks.SubscribeRegistry;
import core.liquid.util.reflect.PublicField;
import core.liquid.util.reflect.PublicMethod;
import core.liquid.util.reflect.ReflectionHelper;
import core.liquid.util.reflect.provide.ClassHandlers;
import core.liquid.util.reflect.provide.ClassHolder;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModInit {
    private static final Type REGISTER = Type.getType(Register.class);
    private static final Type INIT_REGISTRY = Type.getType(Register.Init.class);

    public static synchronized void run(ModContainer modContainer, ModFileScanData scanResults, Object mod) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(EventPriority.HIGHEST, (FMLConstructModEvent event) -> {
            if (!modContainer.matches(mod)) {
                throw new IllegalArgumentException(String.format("Object being provided as mod (%s) doesn't match the one (%s) in the container.", mod.getClass(), modContainer.getMod().getClass()));
            }
        });
        List<LiquidRegistry> registers = new ArrayList<>();
        List<Runnable> init = new ArrayList<>();

        setupAutoRegistries(scanResults, init::add, registers::add);

        SubscribeRegistry.regToBus(registers, modEventBus);
        processInitMethods(init);
    }

    private static void setupAutoRegistries(ModFileScanData scanResults, Consumer<Runnable> initProcess, Consumer<LiquidRegistry> registerSubscriber) {
        scanResults.getAnnotations().stream()
                .filter(annotationData -> annotationData.annotationType().equals(REGISTER) || annotationData.annotationType().equals(INIT_REGISTRY))
                .forEach(annotationData -> {
                    try {
                        String containerClassName = annotationData.clazz().getClassName();
                        Class<?> containerClass;
                        try {
                            containerClass = Class.forName(containerClassName);
                        } catch (Throwable e) {
                            throw new RuntimeException(String.format("There was an exception while trying to load %s", containerClassName), e);
                        }

                        if (annotationData.annotationType().equals(REGISTER)) {
                            registerProcess(containerClass, annotationData, registerSubscriber);
                        } else {
                            initProcess(containerClass, annotationData, initProcess);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static void registerProcess(Class<?> containerClass, ModFileScanData.AnnotationData annotationData, Consumer<LiquidRegistry> registerSubscriber) throws ClassNotFoundException {
        String fieldName = annotationData.memberName();
        PublicField<?, Object> field = ReflectionHelper.findField(containerClass, fieldName);

        regProcess(containerClass, field, registerSubscriber);
    }

    private static void regProcess(Class<?> containerClass, PublicField<?, Object> field, Consumer<LiquidRegistry> registerSubscriber) {
        if (field.isStatic()) {
            if (LiquidRegistry.class.isAssignableFrom(field.unboxed().getType())) {
                LiquidRegistry register = (LiquidRegistry) field.get(null);
                register.setOwner(containerClass);

                registerSubscriber.accept(register);
            } else {
                throw new UnsupportedOperationException(Register.class.getSimpleName() + " can be used only on fields that have " + LiquidRegister.class.getSimpleName() + " type. Error is in: " + field);
            }
        } else {
            throw new UnsupportedOperationException(Register.class.getSimpleName() + " can be used only on static fields. Error is in: " + field);
        }
    }

    private static void initProcess(Class<?> containerClass, ModFileScanData.AnnotationData annotationData, Consumer<Runnable> preConstructMethodRegistrator) throws ClassNotFoundException {
        String methodSignature = annotationData.memberName();

        ClassHolder handler = ClassHandlers.findHandler(containerClass);
        if (handler == null) {
            throw new IllegalArgumentException("Can't handle class " + containerClass.getName() + ", because there's no " + ClassHolder.class.getName() + " found for it.");
        }

        PublicMethod<?, Object> initMethod = handler.findMethod(containerClass, methodSignature);
        if (initMethod == null)
            throw new NoSuchMethodError("Not found method " + methodSignature + " from class " + containerClass.getName());
        handler.requireStatic(initMethod);

        Method nativeMethod = initMethod.unboxed();
        if (nativeMethod.getParameterCount() == 0) {
            preConstructMethodRegistrator.accept(() -> handler.invokeStaticMethod(initMethod));
        } else if (nativeMethod.getParameterCount() == 1 && FMLConstructModEvent.class.isAssignableFrom(nativeMethod.getParameterTypes()[0])) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.HIGHEST, (FMLConstructModEvent event) -> handler.invokeStaticMethod(initMethod, event));
        } else {
            throw new UnsupportedOperationException(Register.class.getSimpleName() + " can be used only on methods with " + FMLConstructModEvent.class.getName() + " parameter or without any parameters. Error is in: " + initMethod);
        }
    }

    private static void processInitMethods(List<Runnable> initMethods) {
        initMethods.forEach(Runnable::run);
    }
}
