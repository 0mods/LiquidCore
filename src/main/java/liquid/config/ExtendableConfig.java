package liquid.config;

import liquid.LiquidCore;
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

    public abstract void reloadContext(ModConfigEvent event);

    public void setConfigSpec(ForgeConfigSpec spec) {
        this.configSpec = spec;
    }

    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    public static <T extends ExtendableConfig> T of(ModConfig.Type type, Class<T> config) {
        return buildingConfig(type, config, false);
    }

    public static <T extends ExtendableConfig> T ofReload(ModConfig.Type type, Class<T> config) {
        return buildingConfig(type, config, true);
    }

    private static <T extends ExtendableConfig> T buildingConfig(ModConfig.Type type, Class<T> tClass, Boolean reloadable) {
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        String modId = modContainer.getModId();

        T creatingConfigFileFromClass; //CCFFC

        try {
            creatingConfigFileFromClass = tClass.getConstructor(ForgeConfigSpec.Builder.class).newInstance(builder);
        } catch (Throwable throwable) {
            throw new IllegalStateException(throwable);
        }

        ForgeConfigSpec spec = builder.build();

        if (modId != null) {
            LiquidCore.log.debug(
                    """
                            Generating config for ModId: {}.\s
                            Config type: {}.\s
                            Config class: {}.\s
                            Config directory: config/liquid/{}/{}.toml
                            """, modId, type, tClass, modId, type.extension()
            );

            ModLoadingContext.get().registerConfig(type, spec, "liquid/" + modId + "/" + type.extension() + ".toml");

        }

        if (reloadable.equals(true)) {
            Consumer<ModConfigEvent> configEventConsumer = event -> {
                if (event.getConfig().getType() == type) {
                    creatingConfigFileFromClass.reloadContext(event);
                }
            };

            FMLJavaModLoadingContext.get().getModEventBus().addListener(configEventConsumer);

        }

        return creatingConfigFileFromClass;
    }
}
