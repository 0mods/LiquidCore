package core.liquid.registry.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import core.liquid.registry.base.LiquidRegistry;
import core.liquid.registry.base.tasks.TaskHandler;
import core.liquid.util.LiquidHolder;
import core.liquid.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class LiquidRegister<T extends IForgeRegistryEntry<T>> extends LiquidRegistry {
    private final IForgeRegistry<T> registry;
    private Map<RegistryObject<T>, Supplier<T>> entries = new HashMap<>();

    private final TaskHandler<Runnable> clientSetupTasks = TaskHandler.of(FMLClientSetupEvent.class);
    private final TaskHandler<Runnable> regEventTasks = TaskHandler.of(RegistryEvent.Register.class);
    private final TaskHandler<Runnable> commonSetupTasks = TaskHandler.of(FMLCommonSetupEvent.class);


    public LiquidRegister(IForgeRegistry<T> forgeRegistry, String modId) {
        super(modId);
        this.registry = forgeRegistry;
    }

    protected <I extends T> RegistryObject<I> registerEntry(String name, Supplier<I> entrySup) {
        Preconditions.checkNotNull(entries, "Cannot register new entries after RegistryEvent.Register has been fired.");

        ResourceLocation registryName = new ResourceLocation(getModId(), name);
        RegistryObject<I> holder = RegistryObject.of(registryName, registry);
        if (entries.put((RegistryObject<T>) holder, () -> entrySup.get().setRegistryName(registryName)) != null) {
            throw new IllegalArgumentException("Attempted to register " + name + " twice for registry " + registry.getRegistryName());
        }

        return holder;
    }

    protected void runOnCommonSetup(Runnable task) {
        commonSetupTasks.add(task);
    }

    protected void runAfterRegistering(Runnable task) {
        regEventTasks.add(task);
    }

    @Override
    public void regToBus(IEventBus modEventBus) {
        super.regToBus(modEventBus);
        modEventBus.register(new EventDispatcher());
        modEventBus.addListener(EventPriority.LOWEST, this::onClientSetup);
        modEventBus.addListener(this::onCommonSetup);
    }

    private void onAllRegEvent(RegistryEvent.Register<? extends IForgeRegistryEntry<?>> event) {
        if (event.getGenericType() == registry.getRegistrySuperType()) {
            onRegEvent(((RegistryEvent.Register<T>) event));
        }
    }

    protected void onRegEvent(RegistryEvent.Register<T> event) {
        IForgeRegistry<T> registry = event.getRegistry();

        LiquidHolder<RegistryObject<T>> currentHolder = new LiquidHolder<>(null);

        catchErrors("registering entries of type " + registry.getRegistrySuperType(), () -> {
            for (Map.Entry<RegistryObject<T>, Supplier<T>> entry : entries.entrySet()) {
                RegistryObject<T> holder = entry.getKey();
                currentHolder.set(holder);

                T value = entry.getValue().get();
                validateEntry(value);
                registry.register(value);

                holder.updateReference(registry);
            }
        }, () -> Lists.newArrayList(
                Pair.of("Registry type", registry.getRegistrySuperType()),
                Pair.of("Currently registering object", currentHolder.get() != null ? currentHolder.get().getId() : null)));

        entries = null;

        catchErrors("finishing register event of type " + registry.getRegistrySuperType(), () -> regEventTasks.eachOrDelete(Runnable::run));
    }

    /**
     * Throws error if the entry is invalid upon the register event
     */
    protected void validateEntry(T entry) {
    }

    protected void onClientSetup(FMLClientSetupEvent event) {
        enqueueWork(event, () -> clientSetupTasks.eachOrDelete(Runnable::run));
    }

    protected void onCommonSetup(FMLCommonSetupEvent event) {
        enqueueWork(event, () -> commonSetupTasks.eachOrDelete(Runnable::run));
    }

    protected IForgeRegistry<T> getRegistry() {
        return registry;
    }

    public static class RegisterChain<I extends IForgeRegistryEntry<? super I>> {
        protected final RegistryObject<I> holder;

        protected RegisterChain(RegistryObject<I> holder) {
            this.holder = holder;
        }

        public RegistryObject<I> asRegistryObject() {
            return holder;
        }

        public ResourceLocation getRegistryName() {
            return asRegistryObject().getId();
        }

        public String getModId() {
            return getRegistryName().getNamespace();
        }

        public String getName() {
            return getRegistryName().getPath();
        }
    }

    public class EventDispatcher {
        @SubscribeEvent
        public void handleEvent(RegistryEvent.Register<?> event) {
            LiquidRegister.this.onAllRegEvent(event);
        }
    }
}
