package liquid;

import liquid.config.ConfigBuilder;
import liquid.config.LiquidConfig;
import liquid.objects.container.Register;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LiquidCore.ModId)
public class LiquidCore {
    public static final Logger log = LoggerFactory.getLogger("LiquidLogger");
    public static final String ModId = "liquid";

    public LiquidCore() {
        ConfigBuilder.build(LiquidConfig.class, "liquid/cfg");
        log.info("LIQUID CORE SUCCESSFULLY STARTED");
        Register.init();
    }
}