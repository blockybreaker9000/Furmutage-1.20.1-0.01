package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TaintedWhitePlanksBlock extends Block {
    public TaintedWhitePlanksBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread only to nearby planks
        if (random.nextInt(4) < 3) { // 75% chance per random tick
            spreadToNearbyBlocks(level, pos, random);
        }
    }

    /**
     * Spreads taint only to nearby planks, wooden stairs, and wooden slabs.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);

                    if (checkState.is(Blocks.OAK_PLANKS) || checkState.is(Blocks.BIRCH_PLANKS) ||
                        checkState.is(Blocks.SPRUCE_PLANKS) || checkState.is(Blocks.JUNGLE_PLANKS) ||
                        checkState.is(Blocks.ACACIA_PLANKS) || checkState.is(Blocks.DARK_OAK_PLANKS) ||
                        checkState.is(Blocks.MANGROVE_PLANKS) || checkState.is(Blocks.CHERRY_PLANKS) ||
                        checkState.is(Blocks.CRIMSON_PLANKS) || checkState.is(Blocks.WARPED_PLANKS)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_PLANKS.get().defaultBlockState(), 3);
                        return;
                    }
                    if (checkState.is(Blocks.OAK_STAIRS) || checkState.is(Blocks.SPRUCE_STAIRS) ||
                        checkState.is(Blocks.BIRCH_STAIRS) || checkState.is(Blocks.JUNGLE_STAIRS) ||
                        checkState.is(Blocks.ACACIA_STAIRS) || checkState.is(Blocks.DARK_OAK_STAIRS) ||
                        checkState.is(Blocks.MANGROVE_STAIRS) || checkState.is(Blocks.CHERRY_STAIRS) ||
                        checkState.is(Blocks.CRIMSON_STAIRS) || checkState.is(Blocks.WARPED_STAIRS)) {
                        BlockState newState = ModBlocks.TAINTED_WHITE_STAIRS.get().defaultBlockState();
                        if (checkState.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
                            newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, checkState.getValue(BlockStateProperties.HORIZONTAL_FACING));
                        if (checkState.hasProperty(BlockStateProperties.HALF))
                            newState = newState.setValue(BlockStateProperties.HALF, checkState.getValue(BlockStateProperties.HALF));
                        if (checkState.hasProperty(BlockStateProperties.STAIRS_SHAPE))
                            newState = newState.setValue(BlockStateProperties.STAIRS_SHAPE, checkState.getValue(BlockStateProperties.STAIRS_SHAPE));
                        level.setBlock(checkPos, newState, 3);
                        return;
                    }
                    if (checkState.is(Blocks.OAK_SLAB) || checkState.is(Blocks.SPRUCE_SLAB) ||
                        checkState.is(Blocks.BIRCH_SLAB) || checkState.is(Blocks.JUNGLE_SLAB) ||
                        checkState.is(Blocks.ACACIA_SLAB) || checkState.is(Blocks.DARK_OAK_SLAB) ||
                        checkState.is(Blocks.MANGROVE_SLAB) || checkState.is(Blocks.CHERRY_SLAB) ||
                        checkState.is(Blocks.CRIMSON_SLAB) || checkState.is(Blocks.WARPED_SLAB)) {
                        BlockState newState = ModBlocks.TAINTED_WHITE_SLAB.get().defaultBlockState();
                        if (checkState.hasProperty(BlockStateProperties.SLAB_TYPE))
                            newState = newState.setValue(BlockStateProperties.SLAB_TYPE, checkState.getValue(BlockStateProperties.SLAB_TYPE));
                        level.setBlock(checkPos, newState, 3);
                        return;
                    }
                }
            }
        }
    }
}

