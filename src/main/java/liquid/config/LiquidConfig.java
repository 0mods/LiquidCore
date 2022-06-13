package liquid.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public final class LiquidConfig extends ExtendableConfig {
    public final ForgeConfigSpec.BooleanValue renderText;
    public final ForgeConfigSpec.ConfigValue<String> prefix;
    public final ForgeConfigSpec.BooleanValue debug;

    public LiquidConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
        builder.push("LiquidCore Config");
        this.renderText = builder.define("screen.dynamic.renderText", false);
        this.prefix = builder.define("jda.prefix", "!");
        this.debug = builder.define("jda.debugText", true);
        builder.pop();
    }

    @Override
    public void ifReloading(ModConfigEvent event) {

    }
}
