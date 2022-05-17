package core.liquid.objects.config;

import com.google.gson.JsonObject;
import core.liquid.objects.annotations.NotWorking;
import core.liquid.objects.enums.LiquidDirectory;
import core.liquid.helper.JsonSaveHelper;

import java.io.File;

@Deprecated(forRemoval = true)
public class MainConfig {
    private static final File config = LiquidDirectory.CONFIG_DIR.get().resolve("liquid.json").toFile();

    public static void initConfig() {
        if (!config.exists()) {
            JsonSaveHelper.save(config, new JsonObject());
        }
    }

    public static void setInt(String target, int value) {
        JsonObject configObject = JsonSaveHelper.readObject(config);
        if (configObject != null) {
            configObject.addProperty(target, value);
            JsonSaveHelper.save(config, configObject);
        } else {
            throw new NullPointerException("Liquid Config corrupted, delete it and try again!");
        }
    }

    public static void setBool(String target, boolean value) {
        JsonObject configObject = JsonSaveHelper.readObject(config);
        if (configObject != null) {
            configObject.addProperty(target, value);
            JsonSaveHelper.save(config, configObject);
        } else {
            throw new NullPointerException("Liquid Config corrupted, delete it and try again!");
        }
    }

    public static void setFloat(String target, float value) {
        JsonObject configObject = JsonSaveHelper.readObject(config);
        if (configObject != null) {
            configObject.addProperty(target, value);
            JsonSaveHelper.save(config, configObject);
        } else {
            throw new NullPointerException("Liquid Config corrupted, delete it and try again!");
        }
    }

    public static boolean tryGetBool(String target, boolean defaultValue) {
        JsonObject obj = JsonSaveHelper.readObject(config);
        if(obj==null) return defaultValue;
        if (!obj.has(target)) {
            obj.addProperty(target, defaultValue);
            JsonSaveHelper.save(config, obj);
            return defaultValue;
        } else {
            return obj.get(target).getAsBoolean();
        }
    }

    public static int tryGetInt(String target, int defaultValue) {
        JsonObject obj = JsonSaveHelper.readObject(config);
        if(obj==null) return defaultValue;
        if (!obj.has(target)) {
            obj.addProperty(target, defaultValue);
            JsonSaveHelper.save(config, obj);
            return defaultValue;
        } else {
            return obj.get(target).getAsInt();
        }
    }

    public static float tryGetFloat(String target, float defaultValue) {
        JsonObject obj = JsonSaveHelper.readObject(config);
        if(obj==null) return defaultValue;
        if (!obj.has(target)) {
            obj.addProperty(target, defaultValue);
            JsonSaveHelper.save(config, obj);
            return defaultValue;
        } else {
            return obj.get(target).getAsFloat();
        }
    }
}
