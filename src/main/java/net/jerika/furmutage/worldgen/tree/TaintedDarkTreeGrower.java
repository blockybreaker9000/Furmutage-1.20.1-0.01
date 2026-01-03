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
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

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
        // Create tree configuration for a tall tree (around 10 blocks)
        TreeConfiguration.TreeConfigurationBuilder builder = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState()),
                new StraightTrunkPlacer(8, 2, 0), // Base height: 8, heightRandA: 2 (so 8-10 blocks tall)
                BlockStateProvider.simple(ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 3), // radius, offset, height
                new TwoLayersFeatureSize(1, 0, 1)
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
                // Place a tall tree manually (around 10 blocks)
                int height = 8 + random.nextInt(3); // 8-10 blocks tall
                for (int i = 0; i < height; i++) {
                    BlockPos logPos = pos.above(i);
                    if (level.getBlockState(logPos).canBeReplaced()) {
                        level.setBlock(logPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                    }
                }
                
                // Add leaves at the top in a blob shape
                BlockPos topPos = pos.above(height);
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        for (int y = 0; y <= 2; y++) {
                            BlockPos leafPos = topPos.offset(x, y, z);
                            // Create a blob shape for leaves
                            double distance = Math.sqrt(x * x + z * z + y * y);
                            if (distance <= 2.5 && level.getBlockState(leafPos).canBeReplaced()) {
                                level.setBlock(leafPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                            }
                        }
                    }
                }
                return true;
            }
            return result;
        } catch (Exception e) {
            // Fallback to manual placement if feature placement fails
            int height = 8 + random.nextInt(3); // 8-10 blocks tall
            for (int i = 0; i < height; i++) {
                BlockPos logPos = pos.above(i);
                if (level.getBlockState(logPos).canBeReplaced()) {
                    level.setBlock(logPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                }
            }
            
            // Add leaves at the top in a blob shape
            BlockPos topPos = pos.above(height);
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = 0; y <= 2; y++) {
                        BlockPos leafPos = topPos.offset(x, y, z);
                        // Create a blob shape for leaves
                        double distance = Math.sqrt(x * x + z * z + y * y);
                        if (distance <= 2.5 && level.getBlockState(leafPos).canBeReplaced()) {
                            level.setBlock(leafPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
            return true;
        }
    }
}

