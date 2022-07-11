package liquid.config;

import com.sun.jdi.ClassNotLoadedException;
import liquid.objects.Constants;
import liquid.objects.annotations.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import liquid.objects.logging.LogHelper;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
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
    private final String name;

    protected ConfigBuilder(Class<?> className, String name) {
        this.className = className;
        this.name = name;
        setup();
    }

    private void setup() {
        final File configDir = FabricLoaderImpl.INSTANCE.getGameDir().resolve("liquidconfig/").toFile();

        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        final File[] configFiles = configDir.listFiles();
        if (configFiles != null) {
            final HashMap<String, JsonObject> configs = new HashMap<>();
            for (File file : configFiles) {
                final String name = "liquid/" + file.getName().substring(0, file.getName().length() - (".json".length()));
                try {
                    final String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    final JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
                    configs.put(name, jsonObject);
                } catch (IOException e) {
                    Constants.LOGGER.error("Failed to load config file: " + file.getName());
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
                Constants.LOGGER.error("Failed to save config file: " + configFile.getAbsolutePath());
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

    public HashMap<String, JsonObject> toJson() {
        final HashMap<Field, Config> fieldMap = getConfigFields();
        final HashMap<String, JsonObject> configs = new HashMap<>();

        for (Map.Entry<Field, Config> entry : fieldMap.entrySet()) {
            Field field = entry.getKey();
            Config annotation = entry.getValue();

            final JsonObject config = configs.computeIfAbsent(this.name, s -> new JsonObject());

            JsonObject categoryObject;
            if (config.has(annotation.group())) {
                categoryObject = config.getAsJsonObject(annotation.group());
            } else {
                categoryObject = new JsonObject();
                config.add(annotation.group(), categoryObject);
            }

            String key = field.getName();
            if (categoryObject.has(key)) {
                throw new UnsupportedOperationException("Some bad happened, duplicate key found: " + key);
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

    public void readFromJson(HashMap<String, JsonObject> configs) {
        final HashMap<Field, Config> fieldMap = getConfigFields();

        for (Map.Entry<Field, Config> entry : fieldMap.entrySet()) {
            Field field = entry.getKey();
            Config annotation = entry.getValue();

            final JsonObject config = configs.get(this.name);

            if (config == null) {
                continue;
            }

            JsonObject categoryObject = config.getAsJsonObject(annotation.group());
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

    public static ConfigBuilder build(Class<?> clazz, String name) {
        if (clazz == null) {
            Constants.LOGGER.error(LogHelper.FATAL, "Config class is null. Starting crash");
            Minecraft.crash(new CrashReport("Config class is null", new ClassNotLoadedException("Config class is null")));
        }

        return new ConfigBuilder(clazz, name);
    }
}
