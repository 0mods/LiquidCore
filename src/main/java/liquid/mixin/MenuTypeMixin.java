package liquid.mixin;

import liquid.LiquidCore;
import liquid.dynamic.container.DynamicContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MenuType.class, remap = false)
public class MenuTypeMixin {
    @Shadow
    private static <T extends AbstractContainerMenu> MenuType<T> register(String pKey, MenuType.MenuSupplier<T> pFactory) {
        return null;
    }

    static {
        LiquidCore.CONTAINER_TYPE = register(new ResourceLocation(LiquidCore.ModId, "dynamic_screen").toString(),
                (windowID, inventory) -> new DynamicContainer(windowID, inventory, inventory.player.getUsedItemHand()));
    }
}
