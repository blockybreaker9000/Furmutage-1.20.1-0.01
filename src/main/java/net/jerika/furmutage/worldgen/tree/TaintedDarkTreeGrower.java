package net.jerika.furmutage.worldgen.tree;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class TaintedDarkTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
        // Return null to use growTree method instead
        return null;
    }

    @Override
    public boolean growTree(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random) {
        // Create tree configuration for a big oak tree (like vanilla big oak)
        // Using MegaJungleTrunkPlacer for the branching trunk structure
        TreeConfiguration.TreeConfigurationBuilder builder = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState()),
                new MegaJungleTrunkPlacer(10, 2, 19), // Base height: 10, heightRandA: 2, heightRandB: 19 (creates tall branching trunk)
                BlockStateProvider.simple(ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), // radius, offset, height (creates large foliage)
                new TwoLayersFeatureSize(0, 0, 0) // Creates multiple layers of foliage
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
            
            // If feature placement failed, try manual placement as fallback (simplified big oak)
            if (!result) {
                // Place a big oak-like tree manually
                int height = 10 + random.nextInt(10); // 10-20 blocks tall
                for (int i = 0; i < height; i++) {
                    BlockPos logPos = pos.above(i);
                    if (level.getBlockState(logPos).canBeReplaced()) {
                        level.setBlock(logPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                    }
                }
                
                // Add large foliage layers at different heights (like big oak)
                int foliageStart = height - 3;
                for (int layer = 0; layer < 3; layer++) {
                    int foliageY = foliageStart + layer * 2;
                    BlockPos foliagePos = pos.above(foliageY);
                    int radius = 3 - layer; // Decreasing radius for each layer
                    
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            for (int y = 0; y <= 1; y++) {
                                BlockPos leafPos = foliagePos.offset(x, y, z);
                                double distance = Math.sqrt(x * x + z * z);
                                if (distance <= radius && level.getBlockState(leafPos).canBeReplaced()) {
                                    level.setBlock(leafPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
                return true;
            }
            return result;
        } catch (Exception e) {
            // Fallback to manual placement if feature placement fails
            int height = 10 + random.nextInt(10); // 10-20 blocks tall
            for (int i = 0; i < height; i++) {
                BlockPos logPos = pos.above(i);
                if (level.getBlockState(logPos).canBeReplaced()) {
                    level.setBlock(logPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                }
            }
            
            // Add large foliage layers at different heights (like big oak)
            int foliageStart = height - 3;
            for (int layer = 0; layer < 3; layer++) {
                int foliageY = foliageStart + layer * 2;
                BlockPos foliagePos = pos.above(foliageY);
                int radius = 3 - layer; // Decreasing radius for each layer
                
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        for (int y = 0; y <= 1; y++) {
                            BlockPos leafPos = foliagePos.offset(x, y, z);
                            double distance = Math.sqrt(x * x + z * z);
                            if (distance <= radius && level.getBlockState(leafPos).canBeReplaced()) {
                                level.setBlock(leafPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}

