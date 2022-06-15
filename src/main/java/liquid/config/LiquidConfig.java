package liquid.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public final class LiquidConfig extends ExtendableConfig {
    public final ForgeConfigSpec.BooleanValue renderText;

    public LiquidConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
        builder.push("LiquidCore Config");
        this.renderText = builder.define("screen.dynamic.renderText", false);
        builder.pop();
    }

    @Override
    public void reloadContext(ModConfigEvent event) {

    }
}
