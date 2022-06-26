package liquid;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiquidCore implements ModInitializer {
    public static final String ModId = "liquid";
    public static final Logger LOGGER = LoggerFactory.getLogger(ModId);

    @Override
    public void onInitialize() {
        LOGGER.info("LiquidCore is loaded!");
    }
}
