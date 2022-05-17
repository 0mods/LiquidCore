package core.liquid.objects.client;

import com.google.gson.JsonObject;
import core.liquid.LiquidCore;
import core.liquid.objects.annotations.NotWorking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

import java.io.*;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@NotWorking(deprecated = true, deprecation = @Deprecated)
public class AutoModel implements PackResources {
    public static List<ResourceLocation> genModels = new ArrayList<>();
    static AutoModel packInstance;
    public final Map<ResourceLocation, IResourceStreamSupplier> resourceMap = new HashMap<>();

    public void addItemModel(ResourceLocation location) {
        ResourceLocation models_item = new ResourceLocation(location.getNamespace(), "models/item/" + location.getPath() + ".json");
        LiquidCore.log.info("register model: " + models_item);
        resourceMap.put(models_item, ofText("{\"parent\":\"item/handheld\",\"textures\":{\"layer0\":\"" + location.getNamespace() + ":items/" + location.getPath() + "\"}}"));
    }

    private static IResourceStreamSupplier ofText(String text) {
        return IResourceStreamSupplier.create(() -> true, () -> new ByteArrayInputStream(text.getBytes()));
    }

    private static IResourceStreamSupplier ofFile(File file) {
        return IResourceStreamSupplier.create(file::isFile, () -> new FileInputStream(file));
    }

    public static AutoModel getPackInstance() {
        if (packInstance == null) packInstance = new AutoModel();
        packInstance.init();
        return packInstance;
    }

    public void init() {
        resourceMap.put(new ResourceLocation(LiquidCore.ModId, "sounds.json"), ofText("{}"));

        for (ResourceLocation location : genModels) {
            addItemModel(location);
        }
    }

    @Override
    public InputStream getRootResource(String filename) throws IOException {
        throw new FileNotFoundException(filename);
    }

    @Override
    public InputStream getResource(PackType packType, ResourceLocation location) throws IOException {
        try {
            return resourceMap.get(location).create();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException)
                throw (IOException) e.getCause();
            throw e;
        }


    }

    @Override
    public Collection<ResourceLocation> getResources(PackType packType, String filename, String name, int p_225637_4_, Predicate<String> p_225637_5_) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasResource(PackType packType, ResourceLocation location) {
        IResourceStreamSupplier s;
        return (s = resourceMap.get(location)) != null && s.exists();

    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        Set<String> hSet = new HashSet<>();
        for (ResourceLocation data : genModels) hSet.add(data.getNamespace());
        return hSet;
    }

    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> deserializer) throws IOException {
        if (deserializer.getMetadataSectionName().equals("pack")) {
            JsonObject obj = new JsonObject();
            obj.addProperty("pack_format", 6);
            obj.addProperty("description", "Generated resources for HollowCore");
            return deserializer.fromJson(obj);
        }
        return null;
    }

    @Override
    public String getName() {
        return "LiquidCore Generated Resources";
    }

    @Override
    public void close() {
    }

    public interface IResourceStreamSupplier {
        static IResourceStreamSupplier create(BooleanSupplier exists, IIOSupplier<InputStream> streamable) {
            return new IResourceStreamSupplier() {
                @Override
                public boolean exists() {
                    return exists.getAsBoolean();
                }

                @Override
                public InputStream create() throws IOException {
                    return streamable.get();
                }
            };
        }

        boolean exists();

        InputStream create() throws IOException;
    }

    @FunctionalInterface
    public interface IIOSupplier<T> {
        T get() throws IOException;
    }
}
