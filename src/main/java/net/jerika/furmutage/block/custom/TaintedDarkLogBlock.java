package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TaintedDarkLogBlock extends RotatedPillarBlock {
    public TaintedDarkLogBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby logs, planks, and leaves - faster than grass and sand
        if (random.nextInt(4) < 3) { // 75% chance per random tick (faster than grass/sand at 50%)
            spreadToNearbyBlocks(level, pos, random);
        }

        // Very rarely spawn dark lynching vines on the underside
        if (random.nextInt(500) == 0) { // 0.2% chance per random tick (very rare)
            spawnVinesUnderneath(level, pos, random);
        }
    }

    /**
     * Spreads taint to nearby logs, planks (woods), and leaves.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks around the log (horizontal and below)
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip self
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // Convert vanilla logs to tainted dark log
                    if (checkState.is(Blocks.OAK_LOG) || checkState.is(Blocks.BIRCH_LOG) || 
                        checkState.is(Blocks.SPRUCE_LOG) || checkState.is(Blocks.JUNGLE_LOG) ||
                        checkState.is(Blocks.ACACIA_LOG) || checkState.is(Blocks.DARK_OAK_LOG) ||
                        checkState.is(Blocks.MANGROVE_LOG) || checkState.is(Blocks.CHERRY_LOG) ||
                        checkState.is(Blocks.CRIMSON_STEM) || checkState.is(Blocks.WARPED_STEM) ||
                        checkState.is(Blocks.STRIPPED_OAK_LOG) || checkState.is(Blocks.STRIPPED_BIRCH_LOG) ||
                        checkState.is(Blocks.STRIPPED_SPRUCE_LOG) || checkState.is(Blocks.STRIPPED_JUNGLE_LOG) ||
                        checkState.is(Blocks.STRIPPED_ACACIA_LOG) || checkState.is(Blocks.STRIPPED_DARK_OAK_LOG) ||
                        checkState.is(Blocks.STRIPPED_MANGROVE_LOG) || checkState.is(Blocks.STRIPPED_CHERRY_LOG) ||
                        checkState.is(Blocks.STRIPPED_CRIMSON_STEM) || checkState.is(Blocks.STRIPPED_WARPED_STEM) ||
                        checkState.is(ModBlocks.STRIPPED_TAINTED_DARK_LOG.get())) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                        return; // Only convert one block per tick to prevent lag
                    }
                    // Convert vanilla planks (woods) to tainted dark planks
                    else if (checkState.is(Blocks.OAK_PLANKS) || checkState.is(Blocks.BIRCH_PLANKS) ||
                        checkState.is(Blocks.SPRUCE_PLANKS) || checkState.is(Blocks.JUNGLE_PLANKS) ||
                        checkState.is(Blocks.ACACIA_PLANKS) || checkState.is(Blocks.DARK_OAK_PLANKS) ||
                        checkState.is(Blocks.MANGROVE_PLANKS) || checkState.is(Blocks.CHERRY_PLANKS) ||
                        checkState.is(Blocks.CRIMSON_PLANKS) || checkState.is(Blocks.WARPED_PLANKS)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_PLANKS.get().defaultBlockState(), 3);
                        return; // Only convert one block per tick to prevent lag
                    }
                    // Convert vanilla leaves to tainted dark leaves
                    else if (checkState.is(Blocks.OAK_LEAVES) || checkState.is(Blocks.BIRCH_LEAVES) ||
                        checkState.is(Blocks.SPRUCE_LEAVES) || checkState.is(Blocks.JUNGLE_LEAVES) ||
                        checkState.is(Blocks.ACACIA_LEAVES) || checkState.is(Blocks.DARK_OAK_LEAVES) ||
                        checkState.is(Blocks.MANGROVE_LEAVES) || checkState.is(Blocks.CHERRY_LEAVES) ||
                        checkState.is(Blocks.AZALEA_LEAVES) || checkState.is(Blocks.FLOWERING_AZALEA_LEAVES) ||
                        checkState.is(Blocks.NETHER_WART_BLOCK) || checkState.is(Blocks.WARPED_WART_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                        return; // Only convert one block per tick to prevent lag
                    }
                }
            }
        }
    }
    
    /**
     * Very rarely spawns dark lynching vines on the underside of this block.
     */
    private void spawnVinesUnderneath(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        
        // Only spawn if the space below is air
        if (belowState.isAir()) {
            level.setBlock(belowPos, ModBlocks.DARK_LYNCHING_VINE.get().defaultBlockState(), 3);
        }
    }
}

