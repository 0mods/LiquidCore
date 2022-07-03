package liquid;

import liquid.config.ConfigBuilder;
import liquid.config.LiquidConfig;
import liquid.objects.logging.LogHelper;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class LiquidCore implements ModInitializer {
    public static final String ModId = "liquid";
    public static final Logger LOGGER = LogHelper.get();

    @Override
    public void onInitialize() {
        LOGGER.info("Loading LiquidCore");
        ConfigBuilder.build(ModId, LiquidConfig.class);
        LOGGER.info("LiquidCore is loaded!");
    }
}
