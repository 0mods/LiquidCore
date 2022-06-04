package liquid.config;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

public abstract class ExtendableConfig {
    protected ForgeConfigSpec configSpec;

    public ExtendableConfig(ForgeConfigSpec.Builder b) {}

    public abstract void ifReloading(ModConfigEvent event);

    public void setConfigSpec(ForgeConfigSpec spec) {
        this.configSpec = spec;
    }

    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    public static <T extends ExtendableConfig> T of(ModConfig.Type type, Class<T> config, String modId) {
        return buildingConfig(type, config, modId, false);
    }

    public static <T extends ExtendableConfig> T ofReload(ModConfig.Type type, Class<T> config, String modId) {
        return buildingConfig(type, config, modId, true);
    }

    private static <T extends ExtendableConfig> T buildingConfig(ModConfig.Type type, Class<T> tClass, String modId, Boolean reloadable) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        T compile;

        try {
            compile = tClass.getConstructor(ForgeConfigSpec.Builder.class).newInstance(builder);
        } catch (Throwable throwable) {
            throw new IllegalStateException(throwable);
        }

        ForgeConfigSpec spec = builder.build();
        ModLoadingContext.get().registerConfig(type, spec, "liquid/" + modId + "/" + type.extension() + ".toml");

        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();

        if (!modId.equals(modContainer.getModId())) {
            Minecraft.crash(
                    new CrashReport(
                            "FATAL. CONFIG MODID: \"" + modId + "\" DOESN'T MATH THE CONTAINER MODID: \"" + modContainer.getModId() + "\"",
                            new RuntimeException("ModId: \"" + modId + "\" is not a ModId: \"" + modContainer.getModId() + "\"!")
                    )
            );
        }

        if (reloadable.equals(true)) {
            Consumer<ModConfigEvent> configEventConsumer = event -> {
                if (event.getConfig().getType() == type) {
                    compile.ifReloading(event);
                }
            };

            FMLJavaModLoadingContext.get().getModEventBus().addListener(configEventConsumer);
        }

        return compile;
    }
}
