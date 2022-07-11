package liquid.test;

import liquid.dynamic.item.DynamicItem;
import liquid.objects.Constants;
import liquid.objects.data.container.DynamicData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ExampleRegTest {
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.ModId);

    public static final RegistryObject<Item> EXAMPLE_DYNAMIC_ITEM = ITEM.register("example_dynamic_item",
            ()-> new DynamicItem(DynamicData.of(1, 1, SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS),
                    new Item.Properties()));
}
