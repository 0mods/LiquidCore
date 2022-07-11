package liquid;

import liquid.dynamic.client.DynamicScreen;
import liquid.objects.Constants;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class LiquidCoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(Constants.CONTAINER_TYPE, DynamicScreen::new);
    }
}
