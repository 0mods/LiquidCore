package example.item;

import core.liquid.dynamic.item.DynamicItem;
import core.liquid.objects.data.DynamicContainerData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;

class DynamicItemExample extends DynamicItem {
    public DynamicItemExample() {
        super(
                DynamicContainerData.create(3, 3, SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS),
                new Properties()
                        .tab(CreativeModeTab.TAB_BREWING)
        );
    }
}
