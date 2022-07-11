package liquid;

import liquid.config.ConfigBuilder;
import liquid.config.LiquidConfig;
import liquid.objects.Constants;
import net.fabricmc.api.ModInitializer;

public class LiquidCore implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigBuilder.build(LiquidConfig.class, Constants.ModId);
        Constants.LOGGER.info("Hello Fabric world!");
    }
}
