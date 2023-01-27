package liquid.objects.reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;

public class ReaderHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Path getLiquidPath() {
        return FMLPaths.GAMEDIR.get().resolve("java/liquid");
    }

    public static Path getAdditionalPath(String pathName) {
        return ReaderHelper.getLiquidPath().resolve(pathName);
    }

    public static File getFileAdditional(String pathName, String fileName) {
        return ReaderHelper.getAdditionalPath(pathName).resolve(fileName).toFile();
    }

    public static File getFileInMain(String fileName) {
        return ReaderHelper.getLiquidPath().resolve(fileName).toFile();
    }

    public static JsonObject getObjectFromAdditionalFile(String pathName, String fileName) {
        return GSON.fromJson(ReaderHelper.getFileAdditional(pathName, fileName + ".json").getName(), JsonObject.class);
    }

    public static JsonObject getObjectFromFile(String fileName) {
        return GSON.fromJson(ReaderHelper.getFileInMain(fileName + ".json").getName(), JsonObject.class);
    }

    public static Path getResources() {
        return ReaderHelper.getLiquidPath().resolve("resources");
    }

    public static File getFileResource(String fileName) {
        return ReaderHelper.getResources().resolve(fileName).toFile();
    }

    private static JsonObject getResourceConfig() {
        var s = GSON.fromJson(ReaderHelper.getFileResource("config.json").getName(), JsonObject.class);

        if (s.get("") == null) {
            s.addProperty("", "");
        }

        return s;
    }
}
