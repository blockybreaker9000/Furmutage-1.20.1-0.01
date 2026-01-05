package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TaintedWhitePlanksBlock extends Block {
    public TaintedWhitePlanksBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby sand, dirt, and grass blocks
        if (random.nextInt(25) == 0) { // 4% chance per random tick
            spreadToNearbyBlocks(level, pos, random);
        }

    }

    /**
     * Spreads taint to nearby planks, sand, dirt, and grass blocks.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks around the planks (horizontal and below)
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip self
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // Convert vanilla planks to tainted white planks
                    if (checkState.is(Blocks.OAK_PLANKS) || checkState.is(Blocks.BIRCH_PLANKS) ||
                        checkState.is(Blocks.SPRUCE_PLANKS) || checkState.is(Blocks.JUNGLE_PLANKS) ||
                        checkState.is(Blocks.ACACIA_PLANKS) || checkState.is(Blocks.DARK_OAK_PLANKS) ||
                        checkState.is(Blocks.MANGROVE_PLANKS) || checkState.is(Blocks.CHERRY_PLANKS) ||
                        checkState.is(Blocks.CRIMSON_PLANKS) || checkState.is(Blocks.WARPED_PLANKS)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_PLANKS.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla logs to tainted white log
                    else if (checkState.is(Blocks.OAK_LOG) || checkState.is(Blocks.BIRCH_LOG) || 
                        checkState.is(Blocks.SPRUCE_LOG) || checkState.is(Blocks.JUNGLE_LOG) ||
                        checkState.is(Blocks.ACACIA_LOG) || checkState.is(Blocks.DARK_OAK_LOG) ||
                        checkState.is(Blocks.MANGROVE_LOG) || checkState.is(Blocks.CHERRY_LOG) ||
                        checkState.is(Blocks.CRIMSON_STEM) || checkState.is(Blocks.WARPED_STEM) ||
                        checkState.is(Blocks.STRIPPED_OAK_LOG) || checkState.is(Blocks.STRIPPED_BIRCH_LOG) ||
                        checkState.is(Blocks.STRIPPED_SPRUCE_LOG) || checkState.is(Blocks.STRIPPED_JUNGLE_LOG) ||
                        checkState.is(Blocks.STRIPPED_ACACIA_LOG) || checkState.is(Blocks.STRIPPED_DARK_OAK_LOG) ||
                        checkState.is(Blocks.STRIPPED_MANGROVE_LOG) || checkState.is(Blocks.STRIPPED_CHERRY_LOG) ||
                        checkState.is(Blocks.STRIPPED_CRIMSON_STEM) || checkState.is(Blocks.STRIPPED_WARPED_STEM)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_LOG.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla leaves to tainted white leaves
                    else if (checkState.is(Blocks.OAK_LEAVES) || checkState.is(Blocks.BIRCH_LEAVES) ||
                        checkState.is(Blocks.SPRUCE_LEAVES) || checkState.is(Blocks.JUNGLE_LEAVES) ||
                        checkState.is(Blocks.ACACIA_LEAVES) || checkState.is(Blocks.DARK_OAK_LEAVES) ||
                        checkState.is(Blocks.MANGROVE_LEAVES) || checkState.is(Blocks.CHERRY_LEAVES) ||
                        checkState.is(Blocks.AZALEA_LEAVES) || checkState.is(Blocks.FLOWERING_AZALEA_LEAVES) ||
                        checkState.is(Blocks.NETHER_WART_BLOCK) || checkState.is(Blocks.WARPED_WART_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_LEAF.get().defaultBlockState(), 3);
                    }
                    // Convert grass blocks to tainted white grass
                    else if (checkState.is(Blocks.GRASS_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_GRASS.get().defaultBlockState(), 3);
                    }
                    // Convert dirt to tainted white dirt
                    else if (checkState.is(Blocks.DIRT) || checkState.is(Blocks.COARSE_DIRT)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_DIRT.get().defaultBlockState(), 3);
                    }
                    // Convert sand to tainted white sand
                    else if (checkState.is(Blocks.SAND) || checkState.is(Blocks.RED_SAND)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_SAND.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}

