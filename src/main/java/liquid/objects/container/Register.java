package liquid.objects.container;

import liquid.LiquidCore;
import liquid.dynamic.container.DynamicContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Register {
    public static final DeferredRegister<MenuType<?>> screen = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            LiquidCore.ModId);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        screen.register(bus);
    }

    public static final RegistryObject<MenuType<DynamicContainer>> DC = screen.register("dynamic_screen", ()->
            IForgeMenuType.create((ID, Inventory, Null)-> new DynamicContainer(ID, Inventory,
                    Inventory.player.getUsedItemHand())));
}
