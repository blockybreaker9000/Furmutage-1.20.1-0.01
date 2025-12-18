package net.jerika.furmutage.worldgen.tree;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class TaintedWhiteTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
        // Return null to use growTree method instead
        return null;
    }

    @Override
    public boolean growTree(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random) {
        // Create tree configuration like a crazy acacia tree with multiple branches
        TreeConfiguration.TreeConfigurationBuilder builder = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.TAINTED_WHITE_LOG.get().defaultBlockState()),
                new ForkingTrunkPlacer(5, 2, 2), // Height: base, heightRandA, heightRandB (creates branching)
                BlockStateProvider.simple(ModBlocks.TAINTED_WHITE_LEAF.get().defaultBlockState()),
                new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2)), // radius, offset
                new TwoLayersFeatureSize(2, 2, 2)
        );
        
        builder.dirt(BlockStateProvider.simple(net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState()));
        TreeConfiguration treeConfig = builder.build();

        // Try to place the tree using the feature
        try {
            ConfiguredFeature<TreeConfiguration, ?> configuredFeature = new ConfiguredFeature<>(Feature.TREE, treeConfig);
            FeaturePlaceContext<TreeConfiguration> context = new FeaturePlaceContext<>(
                Optional.of(configuredFeature),
                level,
                chunkGenerator,
                random,
                pos,
                treeConfig
            );
            boolean result = Feature.TREE.place(context);
            
            // If feature placement failed, try manual placement as fallback
            if (!result) {
                // Place a simple test tree manually - just a few logs and leaves to verify it works
                int height = 5 + random.nextInt(3);
                for (int i = 0; i < height; i++) {
                    level.setBlock(pos.above(i), ModBlocks.TAINTED_WHITE_LOG.get().defaultBlockState(), 3);
                }
                // Add some leaves at the top
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        if (Math.abs(x) + Math.abs(z) <= 2) {
                            level.setBlock(pos.above(height).offset(x, 0, z), ModBlocks.TAINTED_WHITE_LEAF.get().defaultBlockState(), 3);
                        }
                    }
                }
                return true;
            }
            return result;
        } catch (Exception e) {
            // Fallback to manual placement if feature placement fails
            int height = 5 + random.nextInt(3);
            for (int i = 0; i < height; i++) {
                level.setBlock(pos.above(i), ModBlocks.TAINTED_WHITE_LOG.get().defaultBlockState(), 3);
            }
            // Add some leaves at the top
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (Math.abs(x) + Math.abs(z) <= 2) {
                        level.setBlock(pos.above(height).offset(x, 0, z), ModBlocks.TAINTED_WHITE_LEAF.get().defaultBlockState(), 3);
                    }
                }
            }
            return true;
        }
    }
}

