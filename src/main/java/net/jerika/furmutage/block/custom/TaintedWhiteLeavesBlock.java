package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TaintedWhiteLeavesBlock extends LeavesBlock {
    public TaintedWhiteLeavesBlock(Properties properties) {
        super(properties);
    }

    /**
     * Override randomTick to check for tainted white logs within 15 blocks before allowing decay.
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PERSISTENT)) {
            return; // Persistent leaves don't decay
        }
        
        // Check if there's a tainted white log within 15 blocks
        if (hasTaintedWhiteLogWithinRange(level, pos, 15)) {
            // Log found nearby, don't decay - just return without calling super
            return;
        }
        
        // No log nearby, allow normal decay
        super.randomTick(state, level, pos, random);
    }

    /**
     * Checks if there's a tainted white log within the specified range (manhattan distance).
     * Uses a recursive flood-fill-like approach similar to vanilla leaves.
     */
    private boolean hasTaintedWhiteLogWithinRange(BlockGetter level, BlockPos pos, int maxDistance) {
        return checkForLog(level, pos, pos, maxDistance, new java.util.HashSet<>());
    }

    private boolean checkForLog(BlockGetter level, BlockPos originalPos, BlockPos currentPos, int remainingDistance, java.util.Set<BlockPos> visited) {
        if (remainingDistance <= 0) {
            return false;
        }
        
        if (visited.contains(currentPos)) {
            return false;
        }
        
        visited.add(currentPos);
        
        BlockState state = level.getBlockState(currentPos);
        
        // Found a tainted white log!
        if (state.is(ModBlocks.TAINTED_WHITE_LOG.get()) || 
            state.is(ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get())) {
            return true;
        }
        
        // If we hit a solid block that's not a log or leaf, stop searching
        if (!state.isAir() && 
            !(state.getBlock() instanceof LeavesBlock) &&
            !state.is(ModBlocks.TAINTED_WHITE_LOG.get()) &&
            !state.is(ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get())) {
            return false;
        }
        
        // Continue searching in all directions
        for (Direction direction : Direction.values()) {
            BlockPos nextPos = currentPos.relative(direction);
            if (checkForLog(level, originalPos, nextPos, remainingDistance - 1, visited)) {
                return true;
            }
        }
        
        return false;
    }
}

