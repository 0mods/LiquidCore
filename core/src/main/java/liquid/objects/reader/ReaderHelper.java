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
        return FMLPaths.GAMEDIR.get().resolve("liquid");
    }

    public static Path getAdditionalPath(String pathName) {
        return ReaderHelper.getLiquidPath().resolve(pathName);
    }

    public static File getFile(String pathName, String fileName) {
        return ReaderHelper.getAdditionalPath(pathName).resolve(fileName).toFile();
    }

    public static JsonObject getObjectFromFile(String pathName, String fileName) {
        return GSON.fromJson(ReaderHelper.getFile(pathName, fileName + ".json").getName(), JsonObject.class);
    }

    public static Path getResources() {
        return null;
    }
}
