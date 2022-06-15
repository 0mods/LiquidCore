package liquid.mixin;

import liquid.LiquidCore;
import liquid.objects.container.client.DynamicScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MenuScreens.class, remap = false)
public class MenuScreensMixin {
    @Shadow
    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> pType, MenuScreens.ScreenConstructor<M, U> pFactory) {}

    static {
        register(LiquidCore.CONTAINER_TYPE, DynamicScreen::new);
    }
}
