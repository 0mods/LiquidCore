//package liquid.objects.data.gen;
//
//import liquid.LiquidCore;
//import com.google.common.collect.Sets;
//import net.minecraft.advancements.Advancement;
//import net.minecraft.advancements.FrameType;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.CachedOutput;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.DataProvider;
//import net.minecraft.data.PackOutput;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.ItemLike;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import net.minecraftforge.common.data.ForgeAdvancementProvider;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.Consumer;
//
//public class AdvancementGenerator extends ForgeAdvancementProvider {
//    private final Path generatorOutput;
//
//    public AdvancementGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
//        super(output, registries, existingFileHelper, subProviders);
//        this.generatorOutput = output.getOutputFolder();
//    }
//
//    @Override
//    public CompletableFuture<?> run(CachedOutput pCache) {
//        Set<ResourceLocation> set = Sets.newHashSet();
//        Consumer<Advancement> consumer = (advancements -> {
//            if (!set.add(advancements.getId())) {
//                throw new IllegalStateException("Advancement is Dublicated: " + advancements.getId());
//            } else {
//                Path path = createPath(generatorOutput, advancements);
//
//                try {
//                    DataProvider.saveStable(pCache, advancements.deconstruct().serializeToJson(), path);
//                } catch (Exception exception) {
//                    LiquidCore.log.error("Cannot save advancement: {}", path, exception);
//                }
//            }
//        });
//
//        return this
//    }
//
//    private static Path createPath(Path pathIn, Advancement advancementIn) {
//        return pathIn.resolve("data/" + LiquidCore.ModId + "/advancements/" + advancementIn.getId().getPath() + ".json");
//    }
//
//    public static void advancement(Advancement parent, ItemLike display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden, String modId) {
//        Advancement.Builder.advancement().parent(parent).display(display,
//                Component.translatable("advancement." + modId + "." + name),
//                Component.translatable("advancement." + modId + "." + name + ".desc"),
//                null, frame, showToast, announceToChat, hidden);
//    }
//
//    public static void advancement(Advancement parent, ItemStack display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden, String modId) {
//        Advancement.Builder.advancement().parent(parent).display(display,
//                Component.translatable("advancement." + modId + "." + name),
//                Component.translatable("advancement." + modId + "." + name + ".desc"),
//                null, frame, showToast, announceToChat, hidden);
//    }
//}
