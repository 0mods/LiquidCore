package liquid.mixin;

import liquid.dynamic.container.DynamicContainer;
import liquid.objects.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MenuType.class)
public abstract class MenuTypeMixin {
    @Shadow
    private static <T extends AbstractContainerMenu> MenuType<T> register(String $$0, MenuType.MenuSupplier<T> $$1) {
        return null;
    }

    static {
        Constants.CONTAINER_TYPE = register(new ResourceLocation(Constants.ModId, "dynamic_screen").toString(),
                (a,b)-> new DynamicContainer(a, b, b.player.getUsedItemHand()));
    }
}
