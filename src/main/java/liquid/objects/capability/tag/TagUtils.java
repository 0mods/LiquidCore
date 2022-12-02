package liquid.objects.capability.tag;

import liquid.LiquidCore;
import liquid.objects.capability.serializer.LiquidTagSerializer;
import liquid.objects.utils.JVMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
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

    public static final LiquidTagSerializer<AABB> AXIS_SERIALIZER = new LiquidTagSerializer<>("axis") {
        @Override
        public AABB readTag(CompoundTag tag) {
            return new AABB(
                    tag.getDouble("min_x"),
                    tag.getDouble("min_y"),
                    tag.getDouble("min_z"),
                    tag.getDouble("max_x"),
                    tag.getDouble("max_y"),
                    tag.getDouble("max_z")
            );
        }

        @Override
        public CompoundTag toTag(AABB tag) {
            CompoundTag tag1 = new CompoundTag();

            tag1.putDouble("min_x", tag.minX);
            tag1.putDouble("min_y", tag.minY);
            tag1.putDouble("min_z", tag.minZ);

            tag1.putDouble("max_x", tag.maxX);
            tag1.putDouble("max_y", tag.maxY);
            tag1.putDouble("max_z", tag.maxZ);
            return tag1;
        }
    };

    public static final LiquidTagSerializer<String> STRING_SERIALIZER = new LiquidTagSerializer<>("string") {
        @Override
        public String readTag(CompoundTag tag) {
            return tag.getString("value");
        }

        @Override
        public CompoundTag toTag(String tag) {
            CompoundTag tag1 = new CompoundTag();
            tag1.putString("value", tag);
            return tag1;
        }
    };

    public static final LiquidTagSerializer<BlockPos> BLOCK_POS_SERIALIZER = new LiquidTagSerializer<>("block_pos") {
        @Override
        public BlockPos readTag(CompoundTag tag) {
            return new BlockPos(tag.getInt("value_x"), tag.getInt("value_y"), tag.getInt("value_z"));
        }

        @Override
        public CompoundTag toTag(BlockPos tag) {
            CompoundTag _tag = new CompoundTag();
            _tag.putInt("value_x", tag.getX());
            _tag.putInt("value_y", tag.getY());
            _tag.putInt("value_z", tag.getZ());
            return _tag;
        }
    };

    public static final LiquidTagSerializer<CompoundTag> COMPOUND_SEIALIZER = new LiquidTagSerializer<>("compound_nbt") {
        @Override
        public CompoundTag readTag(CompoundTag nbt) {
            return nbt.getCompound("value");
        }

        @Override
        public CompoundTag toTag(CompoundTag value) {
            CompoundTag nbt = new CompoundTag();
            nbt.put("value", value);
            return nbt;
        }
    };

    public static void init() {}

    public static <T> void saveList(CompoundTag tag, String name, ArrayList<T> e, LiquidTagSerializer<T> serializer) {
        tag.putInt(name + "_size", e.size());
        int i = 0;
        for (T value : e) {
            tag.put(name + "_val_" + i, serializer.toTag(value));
            i++;
        }
    }

    public static <T> ArrayList<T> loadList(CompoundTag tag, String name,
                                            LiquidTagSerializer<T>serializer) {
        int size = tag.getInt(name + "_size");
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) list.add(serializer.readTag(tag.getCompound(name + "_val_" + i)));


        return list;
    }

    public static <T> void save(CompoundTag tag, String name, T value,
                                LiquidTagSerializer<T>serializer) {
        tag.put(name, serializer.toTag(value));
    }

    public static <T> void save(FriendlyByteBuf buf, String name, T value,
                                LiquidTagSerializer<T> serializer) {
        CompoundTag tag = new CompoundTag();
        tag.put(name, serializer.toTag(value));
        buf.writeNbt(tag);
    }

    public static <T> T load(CompoundTag tag, String name, LiquidTagSerializer<?> serializer) {
        return (T) serializer.readTag(tag.getCompound(name));
    }

    public static<T>void addSerializer(LiquidTagSerializer<T> serializer, String name) {
        if (name.contains(":")) name = "liquid_tag:" + name;
        SERIALIZERS.put(new ResourceLocation(name), serializer);
    }

    public static String getName(LiquidTagSerializer<?> serializer) {
        for (var serial : SERIALIZERS.entrySet()) {
            if (serial.getValue().equals(serializer)) {
                return serial.getKey().toString();
            }
        }
        return "null";
    }
}
