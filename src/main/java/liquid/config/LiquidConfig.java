package liquid.config;

import liquid.objects.annotations.Config;

public class LiquidConfig {
    @Config
    public static Boolean renderText = true;
    @Config(comment = "Color on dynamic screens(backpacks)")
    public static String colorInText = "0x000000";
}
