package core.liquid.registry.type;

import core.liquid.objects.annotations.Register;
import core.liquid.registry.api.LiquidRegister;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockRegistry extends LiquidRegister<Block> {
    private final ItemRegisrtry ireg;
    public static final String registryName = ItemRegisrtry.registryName;

    public BlockRegistry(String modId) {
        super(ForgeRegistries.BLOCKS, modId);
        this.ireg = new ItemRegisrtry(modId);
    }

    public <B extends Block> StartBlock<B> register(Supplier<B> entrySup) {
        RegistryObject<B> holder = registerEntry(registryName, entrySup);

        return new StartBlock<>(holder);
    }

    @Override
    public void regToBus(IEventBus modEventBus) {
        ireg.regToBus(modEventBus);
    }

    public class StartBlock<B extends Block> extends RegisterChain<B> {
        protected StartBlock(RegistryObject<B> holder) {
            super(holder);
        }

        public StartBlock<B> withItem(Item.Properties props, Consumer<ItemRegisrtry.StartItem<BlockItem>> itemSettings) {
            return item(() -> new BlockItem(asRegistryObject().get(), props), itemSettings);
        }

        private <I extends Item> StartBlock<B> item(Supplier<I> itemSupplier, Consumer<ItemRegisrtry.StartItem<I>> itemSettings) {
            ItemRegisrtry.StartItem<I> itemRegisterChain = BlockRegistry.this.ireg.register(itemSupplier);
            itemSettings.accept(itemRegisterChain);
            return this;
        }
    }
}
