package liquid.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import liquid.LiquidCore;
import liquid.objects.block.annotations.Config;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConfigBuilder {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Class<?> className;
    private final String configName;

    protected ConfigBuilder(Class<?> className) {
        this.className = className;
        this.configName = ModLoadingContext.get().getActiveContainer().getModId() + "_config";
        setup();
    }

    protected ConfigBuilder(Class<?> className, String configName) {
        this.className = className;
        this.configName = configName;
        setup();
    }

    private void setup() {
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        String modId = container.getModId();

        final File configDir = FMLPaths.GAMEDIR.get().resolve("liquidconfig/").toFile();

        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        final File[] configFiles = configDir.listFiles();
        if (configFiles != null) {
            final HashMap<String, JsonObject> configs = new HashMap<>();
            for (File file : configFiles) {
                final String name = file.getName().substring(0, file.getName().length() - (".json".length()));
                try {
                    final String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    final JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
                    configs.put(name, jsonObject);
                } catch (IOException e) {
                    LiquidCore.log.error("Failed to load config file: " + file.getName());
                    e.printStackTrace();
                }
            }
            readFromJson(configs);
        }

        for (Map.Entry<String, JsonObject> entry : toJson().entrySet()) {
            final File configFile = new File(configDir, entry.getKey() + ".json");
            final String jsonStr = GSON.toJson(entry.getValue());
            try {
                FileUtils.writeStringToFile(configFile, jsonStr, StandardCharsets.UTF_8);
            } catch (IOException e) {
                LiquidCore.log.error("Failed to save config file: " + configFile.getAbsolutePath());
                throw new RuntimeException("Failed to save config file: " + configFile.getAbsolutePath(), e);
            }
        }
    }

    private HashMap<Field, Config> getConfigFields() {
        final HashMap<Field, Config> fieldMap = new HashMap<>();
        for (Field field : className.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Config.class)) {
                continue;
            }
            if (!Modifier.isStatic(field.getModifiers())) {
                throw new UnsupportedOperationException("Field \"" + field.getName() + "\" is not static");
            }
            Config annotation = field.getAnnotation(Config.class);
            fieldMap.put(field, annotation);
        }
        return fieldMap;
    }

    private HashMap<String, JsonObject> toJson() {
        final HashMap<Field, Config> fieldMap = getConfigFields();
        final HashMap<String, JsonObject> configs = new HashMap<>();

        for (Map.Entry<Field, Config> entry : fieldMap.entrySet()) {
            Field field = entry.getKey();
            Config annotation = entry.getValue();

            final JsonObject config = configs.computeIfAbsent(this.configName, s -> new JsonObject());

            JsonObject categoryObject;
            if (config.has(annotation.configBranch())) {
                categoryObject = config.getAsJsonObject(annotation.configBranch());
            } else {
                categoryObject = new JsonObject();
                config.add(annotation.configBranch(), categoryObject);
            }

            String key;

            if (annotation.customName().isEmpty()) {
                key = field.getName();
            } else {
                key = annotation.customName();
            }

            if (categoryObject.has(key)) {
                throw new UnsupportedOperationException("Some bad news... Duplicate key found: " + key);
            }

            JsonObject fieldObject = new JsonObject();
            fieldObject.addProperty("comment", annotation.comment());

            Object value;
            try {
                value = field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            JsonElement jsonElement = GSON.toJsonTree(value);
            fieldObject.add("value", jsonElement);

            categoryObject.add(key, fieldObject);
        }

        return configs;
    }

    private void readFromJson(HashMap<String, JsonObject> configs) {
        final HashMap<Field, Config> fieldMap = getConfigFields();

        for (Map.Entry<Field, Config> entry : fieldMap.entrySet()) {
            Field field = entry.getKey();
            Config annotation = entry.getValue();

            final JsonObject config = configs.get(this.configName);

            if (config == null) {
                continue;
            }

            JsonObject categoryObject = config.getAsJsonObject(annotation.configBranch());
            if (categoryObject == null) {
                continue;
            }

            String key = field.getName();
            if (!categoryObject.has(key)) {
                continue;
            }

            JsonObject fieldObject = categoryObject.get(key).getAsJsonObject();
            if (!fieldObject.has("value")) {
                continue;
            }
            JsonElement jsonValue = fieldObject.get("value");
            Class<?> fieldType = field.getType();

            Object fieldValue = GSON.fromJson(jsonValue, fieldType);

            try {
                field.set(null, fieldValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to set field value", e);
            }
        }
    }

    public static void build(Class<?> clazz) {
        new ConfigBuilder(clazz);
    }

    public static void build(Class<?> clazz, String name) {
        new ConfigBuilder(clazz, name);
    }
}

