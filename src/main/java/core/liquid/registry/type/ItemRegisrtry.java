package core.liquid.registry.type;

import core.liquid.registry.api.LiquidRegister;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemRegisrtry extends LiquidRegister<Item> {
    public ItemRegisrtry(String modId) {
        super(ForgeRegistries.ITEMS, modId);
    }

    public <I extends Item> StartRegistry<I> register(String name, Supplier<I> entrySup) {
        RegistryObject<I> holder = this.registerEntry(name, entrySup);

        return new StartRegistry<>(holder);
    }

    public class StartRegistry<I extends Item> extends RegisterChain<I> {
        private StartRegistry(RegistryObject<I> holder) {
            super(holder);
        }

        public StartRegistry<I> registerChain(Consumer<StartRegistry<I>> task) {
            runAfterRegistering(() -> task.accept(this));
            return this;
        }
    }
}
