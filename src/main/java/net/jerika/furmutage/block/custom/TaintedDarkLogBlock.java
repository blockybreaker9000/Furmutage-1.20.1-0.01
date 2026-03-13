package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TaintedDarkLogBlock extends RotatedPillarBlock {
    public TaintedDarkLogBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby logs, planks, stairs, and slabs (wood)
        if (random.nextInt(4) < 3) { // 75% chance per random tick (faster than grass/sand at 50%)
            spreadToNearbyBlocks(level, pos, random);
        }

        // Very rarely spawn dark lynching vines on the underside
        if (random.nextInt(500) == 0) { // 0.2% chance per random tick (very rare)
            spawnVinesUnderneath(level, pos, random);
        }
    }

    /**
     * Spreads taint to nearby logs, planks, wooden stairs, and wooden slabs; does not convert leaves or existing stripped tainted logs.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks around the log (horizontal and below)
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip self
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // Convert only vanilla logs to tainted dark log (never convert stripped tainted — they stay stripped)
                    if (checkState.is(Blocks.OAK_LOG) || checkState.is(Blocks.BIRCH_LOG) ||
                        checkState.is(Blocks.SPRUCE_LOG) || checkState.is(Blocks.JUNGLE_LOG) ||
                        checkState.is(Blocks.ACACIA_LOG) || checkState.is(Blocks.DARK_OAK_LOG) ||
                        checkState.is(Blocks.MANGROVE_LOG) || checkState.is(Blocks.CHERRY_LOG) ||
                        checkState.is(Blocks.CRIMSON_STEM) || checkState.is(Blocks.WARPED_STEM) ||
                        checkState.is(Blocks.STRIPPED_OAK_LOG) || checkState.is(Blocks.STRIPPED_BIRCH_LOG) ||
                        checkState.is(Blocks.STRIPPED_SPRUCE_LOG) || checkState.is(Blocks.STRIPPED_JUNGLE_LOG) ||
                        checkState.is(Blocks.STRIPPED_ACACIA_LOG) || checkState.is(Blocks.STRIPPED_DARK_OAK_LOG) ||
                        checkState.is(Blocks.STRIPPED_MANGROVE_LOG) || checkState.is(Blocks.STRIPPED_CHERRY_LOG) ||
                        checkState.is(Blocks.STRIPPED_CRIMSON_STEM) || checkState.is(Blocks.STRIPPED_WARPED_STEM)) {
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
                        return;
                    }
                    // Convert vanilla wooden stairs to tainted dark stairs (preserve facing, half, shape)
                    else if (checkState.is(Blocks.OAK_STAIRS) || checkState.is(Blocks.SPRUCE_STAIRS) ||
                        checkState.is(Blocks.BIRCH_STAIRS) || checkState.is(Blocks.JUNGLE_STAIRS) ||
                        checkState.is(Blocks.ACACIA_STAIRS) || checkState.is(Blocks.DARK_OAK_STAIRS) ||
                        checkState.is(Blocks.MANGROVE_STAIRS) || checkState.is(Blocks.CHERRY_STAIRS) ||
                        checkState.is(Blocks.CRIMSON_STAIRS) || checkState.is(Blocks.WARPED_STAIRS)) {
                        BlockState newState = ModBlocks.TAINTED_DARK_STAIRS.get().defaultBlockState();
                        if (checkState.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
                            newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, checkState.getValue(BlockStateProperties.HORIZONTAL_FACING));
                        if (checkState.hasProperty(BlockStateProperties.HALF))
                            newState = newState.setValue(BlockStateProperties.HALF, checkState.getValue(BlockStateProperties.HALF));
                        if (checkState.hasProperty(BlockStateProperties.STAIRS_SHAPE))
                            newState = newState.setValue(BlockStateProperties.STAIRS_SHAPE, checkState.getValue(BlockStateProperties.STAIRS_SHAPE));
                        level.setBlock(checkPos, newState, 3);
                        return;
                    }
                    // Convert vanilla wooden slabs to tainted dark slab (preserve slab type)
                    else if (checkState.is(Blocks.OAK_SLAB) || checkState.is(Blocks.SPRUCE_SLAB) ||
                        checkState.is(Blocks.BIRCH_SLAB) || checkState.is(Blocks.JUNGLE_SLAB) ||
                        checkState.is(Blocks.ACACIA_SLAB) || checkState.is(Blocks.DARK_OAK_SLAB) ||
                        checkState.is(Blocks.MANGROVE_SLAB) || checkState.is(Blocks.CHERRY_SLAB) ||
                        checkState.is(Blocks.CRIMSON_SLAB) || checkState.is(Blocks.WARPED_SLAB)) {
                        BlockState newState = ModBlocks.TAINTED_DARK_SLAB.get().defaultBlockState();
                        if (checkState.hasProperty(BlockStateProperties.SLAB_TYPE))
                            newState = newState.setValue(BlockStateProperties.SLAB_TYPE, checkState.getValue(BlockStateProperties.SLAB_TYPE));
                        level.setBlock(checkPos, newState, 3);
                        return;
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

