package net.jerika.furmutage.worldgen.tree;

import net.jerika.furmutage.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
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
        // Create tree configuration similar to mangrove trees but using available placers
        TreeConfiguration.TreeConfigurationBuilder builder = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.TAINTED_WHITE_LOG.get().defaultBlockState()),
                new StraightTrunkPlacer(9, 9, 19), // Height: base, random1, random2 (mangrove-like height)
                BlockStateProvider.simple(ModBlocks.TAINTED_WHITE_LEAF.get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), // radius, radiusRandom, height
                new TwoLayersFeatureSize(1, 0, 2)
        );
        
        builder.dirt(BlockStateProvider.simple(net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState()));
        TreeConfiguration treeConfig = builder.build();

        // Create ConfiguredFeature and use it with FeaturePlaceContext
        ConfiguredFeature<TreeConfiguration, ?> configuredFeature = new ConfiguredFeature<>(Feature.TREE, treeConfig);
        return Feature.TREE.place(new FeaturePlaceContext<>(
            Optional.of(configuredFeature),
            level,
            chunkGenerator,
            random,
            pos,
            treeConfig
        ));
    }
}

