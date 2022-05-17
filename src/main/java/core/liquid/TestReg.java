package core.liquid;

import core.liquid.objects.annotations.Register;
import core.liquid.registry.type.ItemRegisrtry;
import core.liquid.util.Hooks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TestReg {
    @Register
    public static final ItemRegisrtry ITEMS = new ItemRegisrtry(LiquidCore.ModId);

    public static final Item LIQUID_ITEM_TEST = Hooks.create();

    @Register.Init
    private static void init() {
        ITEMS.register("heheha", ()-> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
    }
}
