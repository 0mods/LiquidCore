package core.liquid.registry.type;

import core.liquid.objects.annotations.Register;
import core.liquid.registry.api.LiquidRegister;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemRegisrtry extends LiquidRegister<Item> {
    public static String registryName;

    public ItemRegisrtry(String modId) {
        super(ForgeRegistries.ITEMS, modId);
    }

    public <I extends Item> StartItem<I> register(Supplier<I> entrySup) {
        RegistryObject<I> holder = this.registerEntry(registryName, entrySup);

        return new StartItem<>(holder);
    }

    public class StartItem<I extends Item> extends RegisterChain<I> {
        public I value;

        private StartItem(RegistryObject<I> holder) {
            super(holder);
            this.value = holder.get();
        }

        public StartItem<I> registerChain(Consumer<StartItem<I>> task) {
            runAfterRegistering(() -> task.accept(this));
            return this;
        }

        public I get() {
            I ret = this.value;
            Objects.requireNonNull(ret, () -> "Registry not present: " + ret.getRegistryName());
            return ret;
        }
    }
}
