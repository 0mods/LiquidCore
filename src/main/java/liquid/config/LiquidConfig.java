package liquid.config;

import liquid.objects.block.annotations.Config;

public class LiquidConfig {
    @Config(comment = "render text on dynamic screens")
    public static Boolean renderText = true;

    @Config(comment = "Color on dynamic screens")
    public static String colorInText = "0x000000";

    @Config(comment = "Draw custom starter screen", customName = "starterScreen", configBranch = "minecraftModification")
    public static Boolean startScreen = false;
}
