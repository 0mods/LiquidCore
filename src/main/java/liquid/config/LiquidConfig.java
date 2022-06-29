package liquid.config;

import liquid.objects.annotations.Config;

public class LiquidConfig {
    @Config(name = "general")
    public static Boolean renderTextOnDynamicScreen = false;

}
