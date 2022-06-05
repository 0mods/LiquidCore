package liquid.config;

import liquid.LiquidCore;
import liquid.objects.annotations.Config;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.function.Consumer;

abstract class ConfigBuilder {
    private static final Type CONFIG_TYPE = Type.getType(Config.class);
    protected ForgeConfigSpec configSpec;

    public ConfigBuilder(ForgeConfigSpec.Builder b) {}

    public abstract void ifReloading(ModConfigEvent event);

    public void setConfigSpec(ForgeConfigSpec spec) {
        this.configSpec = spec;
    }

    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

//    public static <T extends ConfigBuilder> T of(ModConfig.Type type, Class<T> config) {
//        return buildingConfig(type, config, false);
//    }

    public static <T extends ConfigBuilder> T of(Class<T> config) {
        return buildingConfig(config);
    }

    private static <T extends ConfigBuilder> T buildingConfig(Class<T> tClass) {
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        IModInfo modInfo = modContainer.getModInfo();
        ModFileScanData scanData = modInfo.getOwningFile().getFile().getScanResult();

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        T compile;

            try {
                compile = tClass.getConstructor(ForgeConfigSpec.Builder.class).newInstance(builder);
            } catch (Throwable throwable) {
                throw new IllegalStateException(throwable);
            }

            scanData.getAnnotations().stream().filter(aData -> aData.annotationType().equals(CONFIG_TYPE)).forEach(
                    annotationData -> {
                        if (scanData.getAnnotations().equals(CONFIG_TYPE)) {
                            String fieldName = annotationData.memberName();
                            Field field = findField(fieldName);
                            field.setAccessible(true);
                            boolean reloadable = field.getAnnotation(Config.class).reloadable();
                            ModConfig.Type type = field.getAnnotation(Config.class).type();

                            String modId = modContainer.getModId();

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
                            if (reloadable) {
                                Consumer<ModConfigEvent> configEventConsumer = event -> {
                                    if (event.getConfig().getType() == type) {
                                        compile.ifReloading(event);
                                    }
                                };

                                FMLJavaModLoadingContext.get().getModEventBus().addListener(configEventConsumer);
                            }
                        }
                    }
            );
            return compile;
    }

    public static Field findField(String fieldName) {
        try {
            return Config.class.getDeclaredField(fieldName);
        } catch (Throwable e) {
            throw new RuntimeException("Can't retrieve field " + fieldName + " from class " + Config.class, e);
        }
    }
}

