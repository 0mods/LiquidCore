package core.liquid;

import core.liquid.objects.annotations.Register;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class Registration {
    @Register
    public static Item test_item = new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD));
}
