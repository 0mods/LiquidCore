package liquid;

import liquid.config.ExtendableConfig;
import liquid.config.LiquidConfig;
import liquid.dynamic.container.DynamicContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LiquidCore.ModId)
public class LiquidCore {
    public static final Logger log = LogManager.getLogger(LiquidCore.class);
    public static final String ModId = "liquid";

    public static MenuType<DynamicContainer> CONTAINER_TYPE;

    public static LiquidConfig COMMON;

    public LiquidCore() {
        COMMON = ExtendableConfig.of(ModConfig.Type.COMMON, LiquidConfig.class, ModId);
        log.info("LIQUID CORE SUCCESSFULLY STARTED");
    }
}