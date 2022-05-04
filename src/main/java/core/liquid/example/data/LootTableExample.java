package core.liquid.example.data;

import core.liquid.objects.data.gen.LootTableProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;

class LootTableExample extends LootTableProviderBase {
    public LootTableExample(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void addTables() {
        lootTable.put(Blocks.ACACIA_LOG, createWithoutBlockEntity(
                Blocks.ACACIA_LOG.getRegistryName().getPath(),
                Blocks.ACACIA_LOG
        ));
    }
}
