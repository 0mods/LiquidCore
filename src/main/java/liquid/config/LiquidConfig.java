package liquid.config;

import liquid.objects.annotations.Config;

public class LiquidConfig {
    @Config(comment = "render text on dynamic screens")
    public static Boolean renderText = true;

    @Config(comment = "Color on dynamic screens")
    public static String colorInText = "0x000000";

    @Config(comment = "Enable Food Saturation", customName = "foodSaturation")
    public static Boolean fs = false;
}
