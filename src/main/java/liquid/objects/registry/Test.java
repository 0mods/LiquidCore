package liquid.objects.registry;

import net.minecraft.world.item.Item;

public class Test extends RegistryBase {
    public static Item nonItem;

    @Override
    public void init() {
        setModId("liquid");
        item("a", ()-> new Item(new Item.Properties()));
    }
}
