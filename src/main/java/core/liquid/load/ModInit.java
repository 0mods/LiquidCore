package core.liquid.load;

import core.liquid.objects.annotations.Register;
import core.liquid.objects.block.BlockItemProperty;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

class ModInit {
    private static final Type REGISTER = Type.getType(Register.class);

    public static synchronized void run(String modId, ModFileScanData scanResults) {
        startRegisrtry(scanResults, modId);
    }
    private static void startRegisrtry(ModFileScanData scanResults, String modId) {
        DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
        DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, modId);

        scanResults.getAnnotations().stream()
                .filter(annotationData ->
                        annotationData.annotationType().equals(REGISTER)

                )
                .forEach(annotationData -> {
                            String containerClassName = annotationData.clazz().getClassName();
                            Class<?> containerClass;
                            try {
                                containerClass = Class.forName(containerClassName);
                            } catch (Throwable e) {
                                throw new RuntimeException(String.format("There was an exception while trying to load %s", containerClassName), e);
                            }

                            if (annotationData.annotationType().equals(REGISTER)) {
                                String fieldName = annotationData.memberName();
                                Field field = findField(containerClass, fieldName);
                                field.setAccessible(true);
                                String regName = field.getAnnotation(Register.class).registryName();

                                if (Modifier.isStatic(field.getModifiers())) {
                                    try {
                                        Object someObject = field.get(null);

                                        if (someObject instanceof Block block) {
                                            BLOCKS.register(regName, () -> block);
                                            if (block instanceof BlockItemProperty property) {
                                                ITEMS.register(regName, () -> new BlockItem(block, property.p()));
                                            }
                                        } else if (someObject instanceof Item item) {
                                            ITEMS.register(regName, () -> item);
                                        }
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                );

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Throwable e) {
            throw new RuntimeException("Can't retrieve field " + fieldName + " from class " + clazz, e);
        }
    }
}
