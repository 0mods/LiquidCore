package liquid.objects.capability.tag;

import liquid.LiquidCore;
import liquid.objects.capability.serializer.LiquidTagSerializer;
import liquid.objects.utils.JVMUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Map;

public class TagUtils {
    public static final Map<ResourceLocation, LiquidTagSerializer<?>> SERIALIZERS = new HashMap<>();
    public static final LiquidTagSerializer<Void> EMPTY = new LiquidTagSerializer<>("notag") {
        @Override
        public Void readTag(CompoundTag tag) {
            return null;
        }

        @Override
        public CompoundTag toTag(Void tag) {
            return new CompoundTag();
        }
    };
    public static final LiquidTagSerializer<File> FILE_SERIALIZER = new LiquidTagSerializer<>("file_serializer") {
        @Override
        public File readTag(CompoundTag tag) {
            var fileName = tag.getString("name");
            var bytes = tag.getByteArray("data");
            var path = FMLPaths.GAMEDIR.get().resolve("liquidconfig/")
                    .resolve("cache").resolve(fileName);
            var file = path.toFile();
            JVMUtils.initPath(file);

            try {
                Files.write(path, bytes, new OpenOption[0]);
            } catch (Exception e) {
                LiquidCore.log.error("Can't create file!");
                e.printStackTrace();
            }

            return file;
        }

        @Override
        public CompoundTag toTag(File tag) {
            try {
                var stream = Files.newInputStream(tag.toPath());
                var bytes = new byte[stream.available()];
                var dataStream = new DataInputStream(stream);
                dataStream.readFully(bytes);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putString("name", tag.getName());
                compoundTag.putByteArray("data", bytes);
                return compoundTag;
            } catch (Exception e) {
                return new CompoundTag();
            }
        }
    };

    public static<T>void addSerializer(LiquidTagSerializer<T> serializer, String name) {
        if (name.contains(":")) name = "liquid_tag:" + name;
        SERIALIZERS.put(new ResourceLocation(name), serializer);
    }
}
