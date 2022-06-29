package liquid.world;

import liquid.LiquidCore;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class OreGen {
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> configuredHolder(String id, Block block, RuleTest replaceRule, int size) {
        BlockState defaultState = block.defaultBlockState();
        return FeatureUtils.register(id, Feature.ORE, new OreConfiguration(replaceRule, defaultState, size));
    }

    public static Holder<PlacedFeature> placedHolder(String id, Holder<ConfiguredFeature<OreConfiguration, ?>> configuredFeatureHolder, int size, int minHeight, int maxHeight) {
        return PlacementUtils.register(id, configuredFeatureHolder, orePlacement(size, HeightRangePlacement.uniform(
                VerticalAnchor.absolute(minHeight),
                VerticalAnchor.absolute(maxHeight)
        )));
    }

    public static void configuredRegister(String id, Holder<ConfiguredFeature<OreConfiguration, ?>> featureToRegistry) {
        ConfiguredFeature<OreConfiguration, ?> feature = featureToRegistry.value();
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(LiquidCore.ModId, id), feature);
    }

    public static void placedRegister(String id, Holder<PlacedFeature> featureToRegistry) {
        PlacedFeature feature = featureToRegistry.value();
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(LiquidCore.ModId, id), feature);
    }

    private static List<PlacementModifier> orePlace(PlacementModifier placementModifier, PlacementModifier modifier) {
        return List.of(placementModifier, InSquarePlacement.spread(), modifier, BiomeFilter.biome());
    }

    private static List<PlacementModifier> orePlacement(int size, PlacementModifier placementModifier) {
        return orePlace(CountPlacement.of(size), placementModifier);
    }
}
