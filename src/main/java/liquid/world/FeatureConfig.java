package liquid.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class FeatureConfig {
    public static final Codec<FeatureConfig> CODEC = RecordCodecBuilder.create((featureConfigInstance)
            -> featureConfigInstance.group(RuleTest.CODEC.fieldOf("target").forGetter((featureConfig)
            -> featureConfig.target), BlockState.CODEC.fieldOf("state").forGetter((featureConfig)
            -> featureConfig.state), Codec.intRange(0, 64).fieldOf("size").forGetter((featureConfig)
            -> featureConfig.size)).apply(featureConfigInstance, FeatureConfig::new)
    );
    public final RuleTest target;
    public final int size;
    public final BlockState state;

    public FeatureConfig(RuleTest test, BlockState state, int size) {
        this.size = size;
        this.state = state;
        this.target = test;

    }

    public static RuleTest of(Block block) {
        return new BlockMatchTest(block);
    }

    public static RuleTest of(String byName) {
        return new BlockMatchTest(Registry.BLOCK.get(new ResourceLocation(byName)));
    }

    public static RuleTest of(ResourceLocation byLocation) {
        return new BlockMatchTest(Registry.BLOCK.get(byLocation));
    }

    public static final class FillContext {
        public static final RuleTest END_STONE = FeatureConfig.of(Blocks.END_STONE);
    }
}
