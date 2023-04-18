package liquid.objects.model.resource;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BooleanSupplier;

public class LiquidResourceGenPack implements PackResources {
    public static List<ResourceLocation> genItemModels = new ArrayList<>();
    public static List<ResourceLocation> genBlockModel = new ArrayList<>();
    public static List<ResourceLocation> genParticles = new ArrayList<>();
    static LiquidResourceGenPack instance;
    public final Map<ResourceLocation, ResourceStreamSupplier> resourceMap = new HashMap<>();
    @NotNull
    public static final Map<String, JsonObject> genSoundEngine = new HashMap<>();

    private void toSystemItemModel(ResourceLocation l) {
        ResourceLocation itemModel = new ResourceLocation(l.getNamespace(),
                "models/item/" + l.getPath() + ".json");
        resourceMap.put(itemModel,
                textTransformation("{\"parent\":\"item/handheld\",\"textures\":{\"layer0\":\"" + l.getNamespace()
                        + ":items/" + l.getPath() + "\"}}"));
    }
    //F***ED PARTICLE ENGINE!!!
    private void toSystemParticleEngine(ResourceLocation l) {
        ResourceLocation particle = new ResourceLocation(l.getNamespace(), "particles/" + l.getPath() + ".json");
        resourceMap.put(particle, textTransformation("{\"textures\":[\""+l+"\"]}"));
    }

    private void toSystemBlockModel(ResourceLocation location) {
        ResourceLocation blockState = new ResourceLocation(location.getNamespace(), "blockstates/" + location.getPath() + ".json");
        ResourceLocation model = new ResourceLocation(location.getNamespace(), "models/item/" + location.getPath() + ".json");
        resourceMap.put(blockState, textTransformation("{\"variants\":{\"\":{\"model\":\""+location.getNamespace()+":item/"+location.getPath()+"\"}}}"));
        resourceMap.put(model, textTransformation("{\"parent\":\"block/cube_all\",\"textures\":{\"all\":\""+location.getNamespace()+":blocks/"+location.getPath()+"\"}}"));
    }

    private void toSystemSoundJson(String modid, JsonObject sound) {
        resourceMap.put(new ResourceLocation(modid, "sounds.json"),
                textTransformation(sound.toString()));
    }

    private static ResourceStreamSupplier textTransformation(String text) {
        return ResourceStreamSupplier.make(()-> true, ()-> new ByteArrayInputStream(text.getBytes()));
    }

    private static ResourceStreamSupplier fileTransformation(File file) {
        return ResourceStreamSupplier.make(file::isFile, ()-> Files.newInputStream(file.toPath()));
    }

    public static LiquidResourceGenPack getInstance() {
        if (instance == null) instance = new LiquidResourceGenPack();
        instance.init();
        return instance;
    }

    public void init() {
        for (ResourceLocation loc : genItemModels) toSystemItemModel(loc);
        for (ResourceLocation loc : genParticles) toSystemParticleEngine(loc);
        for (ResourceLocation loc : genBlockModel) toSystemBlockModel(loc);
        for (Map.Entry<String , JsonObject> sound : genSoundEngine.entrySet())
            toSystemSoundJson(sound.getKey(), sound.getValue());
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... p_252049_) {
        try {
            throw new FileNotFoundException(Arrays.toString(p_252049_));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType p_215339_, ResourceLocation p_249034_) {
        try {
            return ()-> resourceMap.get(p_249034_).create();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                try {
                    throw (IOException) e.getCause();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw e;
        }
    }

    @Override
    public void listResources(PackType p_10289_, String p_251379_, String p_251932_, ResourceOutput p_249347_) {

    }

    @Override
    public Set<String> getNamespaces(PackType p_10283_) {
        Set<String> hSt = new HashSet<>();
        for (ResourceLocation rd : resourceMap.keySet()) hSt.add(rd.getNamespace());
        return hSt;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> p_10291_) throws IOException {
        if (p_10291_.getMetadataSectionName().equals("pack")) {
            JsonObject object = new JsonObject();
            object.addProperty("pack_format", 8);
            object.addProperty("description", "Generated resources with automatic generation using LiquidCore");
            return p_10291_.fromJson(object);
        }
        return null;
    }


    @Override
    public String packId() {
        return "LC Generated";
    }

    @Override
    public void close() {

    }

    public interface ResourceStreamSupplier {
        static ResourceStreamSupplier make(BooleanSupplier exists, InternalIOSupplier<InputStream> stream) {
            return new ResourceStreamSupplier() {
                @Override
                public boolean exists() {
                    return exists.getAsBoolean();
                }

                @Override
                public InputStream create() throws IOException {
                    return stream.get();
                }
            };
        }

        boolean exists();

        InputStream create() throws IOException;
    }

    @FunctionalInterface
    public interface InternalIOSupplier<T> {
        T get() throws IOException;
    }
}
