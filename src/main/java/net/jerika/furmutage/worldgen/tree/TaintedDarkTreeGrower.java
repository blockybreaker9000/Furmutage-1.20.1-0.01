package net.jerika.furmutage.worldgen.tree;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
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
            
            // Ensure there's a log at the sapling position (the feature might leave air or the sapling there)
            BlockState posState = level.getBlockState(pos);
            if (result && (!posState.is(ModBlocks.TAINTED_DARK_LOG.get()) && posState.canBeReplaced())) {
                level.setBlock(pos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
            }
            
            // Add branches to the tree if feature placement succeeded
            if (result) {
                addBranchesToTree(level, pos, random);
                // Add vines to leaves and lynching vines to branches after tree generation
                addVinesToLeaves(level, pos, random);
                addLynchingVinesToBranches(level, pos, random);
            }
            
            // If feature placement failed, try manual placement as fallback (simplified big oak)
            if (!result) {
                // Place a big oak-like tree manually
                int height = 10 + random.nextInt(10); // 10-20 blocks tall
                // Start from the sapling position (replace the sapling with the first log)
                for (int i = 0; i < height; i++) {
                    BlockPos logPos = pos.above(i);
                    // Always place the first log at the sapling position, then check for replaceable blocks above
                    if (i == 0 || level.getBlockState(logPos).canBeReplaced()) {
                        level.setBlock(logPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                    }
                }
                
                // Add branches to the tree
                addBranchesToTree(level, pos, random);
                
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
                // Add vines to leaves and lynching vines to branches
                addVinesToLeaves(level, pos, random);
                addLynchingVinesToBranches(level, pos, random);
                return true;
            }
            return result;
        } catch (Exception e) {
            // Fallback to manual placement if feature placement fails
            int height = 10 + random.nextInt(10); // 10-20 blocks tall
            // Start from the sapling position (replace the sapling with the first log)
            for (int i = 0; i < height; i++) {
                BlockPos logPos = pos.above(i);
                // Always place the first log at the sapling position, then check for replaceable blocks above
                if (i == 0 || level.getBlockState(logPos).canBeReplaced()) {
                    level.setBlock(logPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                }
            }
            
            // Add branches to the tree
            addBranchesToTree(level, pos, random);
            
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
            // Add vines to leaves and lynching vines to branches
            addVinesToLeaves(level, pos, random);
            addLynchingVinesToBranches(level, pos, random);
            return true;
        }
    }
    
    /**
     * Adds branch paths (horizontal logs) extending from the main trunk.
     */
    private void addBranchesToTree(ServerLevel level, BlockPos trunkBase, RandomSource random) {
        // Find the height of the trunk by checking for logs above
        int trunkHeight = 0;
        for (int i = 0; i < 30; i++) {
            BlockPos checkPos = trunkBase.above(i);
            if (level.getBlockState(checkPos).is(ModBlocks.TAINTED_DARK_LOG.get())) {
                trunkHeight = i + 1;
            } else {
                break;
            }
        }
        
        if (trunkHeight < 5) {
            return; // Tree too short for branches
        }
        
        // Add 3-6 branches at different heights
        int branchCount = 3 + random.nextInt(4); // 3-6 branches
        Direction[] directions = {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST,
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST // Allow duplicates for more variety
        };
        
        for (int i = 0; i < branchCount; i++) {
            // Branch height: between 40% and 80% of trunk height
            int branchHeight = (int)(trunkHeight * (0.4f + random.nextFloat() * 0.4f));
            BlockPos branchStart = trunkBase.above(branchHeight);
            
            // Only add branch if there's a log at the branch start position
            if (!level.getBlockState(branchStart).is(ModBlocks.TAINTED_DARK_LOG.get())) {
                continue;
            }
            
            // Random direction for the branch
            Direction branchDir = directions[random.nextInt(directions.length)];
            
            // Branch length: 2-4 blocks
            int branchLength = 2 + random.nextInt(3);
            
            // Place branch logs
            for (int j = 1; j <= branchLength; j++) {
                BlockPos branchPos = branchStart.relative(branchDir, j);
                
                // Sometimes add a slight upward or downward angle
                if (j > 1 && random.nextInt(3) == 0) {
                    if (random.nextBoolean()) {
                        branchPos = branchPos.above();
                    } else {
                        branchPos = branchPos.below();
                    }
                }
                
                if (level.getBlockState(branchPos).canBeReplaced()) {
                    level.setBlock(branchPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                }
            }
        }
    }
    
    /**
     * Adds tainted dark vines to some leaf blocks in the tree (chance to grow on leaves).
     */
    private void addVinesToLeaves(ServerLevel level, BlockPos treeBase, RandomSource random) {
        // Search in a radius around the tree base for leaves
        int searchRadius = 15;
        int searchHeight = 25;
        
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = 0; y <= searchHeight; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos checkPos = treeBase.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // If this is a leaf block, 15% chance to add vines
                    if (checkState.is(ModBlocks.TAINTED_DARK_LEAF.get()) && random.nextInt(100) < 15) {
                        // Try to place vine on a random side of the leaf
                        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
                        Direction vineDir = directions[random.nextInt(directions.length)];
                        BlockPos vinePos = checkPos.relative(vineDir);
                        
                        // Only place vine if the position is air
                        if (level.getBlockState(vinePos).isAir()) {
                            BlockState vineState = ModBlocks.TAINTED_DARK_VINE.get().defaultBlockState();
                            BooleanProperty property = VineBlock.PROPERTY_BY_DIRECTION.get(vineDir.getOpposite());
                            if (property != null) {
                                vineState = vineState.setValue(property, true);
                                level.setBlock(vinePos, vineState, 3);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Adds dark lynching vines to some log blocks (branches) in the tree.
     */
    private void addLynchingVinesToBranches(ServerLevel level, BlockPos treeBase, RandomSource random) {
        // Search in a radius around the tree base for logs
        int searchRadius = 15;
        int searchHeight = 25;
        
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = 0; y <= searchHeight; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos checkPos = treeBase.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // If this is a log block (branch), 20% chance to add lynching vine
                    if (checkState.is(ModBlocks.TAINTED_DARK_LOG.get()) && random.nextInt(100) < 20) {
                        // Only add lynching vine if the log has air below it (branch hanging)
                        BlockPos belowPos = checkPos.below();
                        if (level.getBlockState(belowPos).isAir()) {
                            // Check if there's already a lynching vine below
                            BlockPos vineCheckPos = belowPos;
                            boolean canPlace = true;
                            for (int i = 0; i < 3; i++) {
                                BlockState vineCheckState = level.getBlockState(vineCheckPos);
                                if (vineCheckState.is(ModBlocks.DARK_LYNCHING_VINE.get()) || 
                                    vineCheckState.is(ModBlocks.DARK_LYNCHING_VINE_PLANT.get())) {
                                    canPlace = false;
                                    break;
                                }
                                vineCheckPos = vineCheckPos.below();
                            }
                            
                            if (canPlace) {
                                // Place the lynching vine head block
                                level.setBlock(belowPos, ModBlocks.DARK_LYNCHING_VINE.get().defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        }
    }
}

