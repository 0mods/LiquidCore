package liquid.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public final class LiquidConfig extends ExtendableConfig {
    public final ForgeConfigSpec.BooleanValue renderText;

    public LiquidConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
        this.renderText = builder.define("renderTextOnDynamicScreen", false);
    }

    @Override
    public void ifReloading(ModConfigEvent event) {

    }
}
