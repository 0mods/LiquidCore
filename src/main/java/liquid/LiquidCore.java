package liquid;

import liquid.config.ConfigBuilder;
import liquid.config.LiquidConfig;
import liquid.dynamic.container.DynamicContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LiquidCore.ModId)
public class LiquidCore {
    public static final Logger log = LogManager.getLogger("LiquidCore Logger");
    public static final String ModId = "liquid";

    public static MenuType<DynamicContainer> CONTAINER_TYPE;

    public LiquidCore() {
        ConfigBuilder.build(LiquidConfig.class, "liquid/cfg");
        log.info("LIQUID CORE SUCCESSFULLY STARTED");
    }
}