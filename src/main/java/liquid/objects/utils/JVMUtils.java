package liquid.objects.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JVMUtils {
    public static InputStream getResource(ResourceLocation location) {
        try {
            return Minecraft.getInstance().getResourceManager().getResource(location).get().open();
        } catch (Exception var2) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/" + location.getNamespace() + "/" + location.getPath());
        }
    }

    public static void nothing() {
    }

    public static <R, K extends R> K castDarkMagic(R original) {
        return (K) original;
    }

    public static void initPath(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static int getResourceLocationSize(ResourceLocation location) throws IOException {
        return Minecraft.getInstance().getResourceManager().getResource(location).get().open().available();
    }

    public static int getInputStreamSize(InputStream inputStream) throws IOException {
        return inputStream.available();
    }
}
