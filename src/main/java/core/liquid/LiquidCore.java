package core.liquid;

import core.liquid.dynamic.container.DynamicContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LiquidCore.ModId)
public class LiquidCore {
    public static final Logger log = LogManager.getLogger(LiquidCore.class);
    public static final String ModId = "liquid";

    public static MenuType<DynamicContainer> CONTAINER_TYPE;

    public LiquidCore() {
        log.info("LIQUID CORE SUCCESSFULLY STARTED");
    }
}