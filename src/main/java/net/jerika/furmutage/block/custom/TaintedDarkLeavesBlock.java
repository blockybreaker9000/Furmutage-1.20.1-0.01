package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TaintedDarkLeavesBlock extends LeavesBlock {
    public TaintedDarkLeavesBlock(Properties properties) {
        super(properties);
    }

    /**
     * Override randomTick to check for tainted dark logs within 15 blocks before allowing decay.
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PERSISTENT)) {
            return; // Persistent leaves don't decay
        }
        
        // Check if there's a tainted dark log within 15 blocks
        if (hasTaintedDarkLogWithinRange(level, pos, 8)) {
            // Log found nearby, don't decay - just return without calling super
            // But still allow spreading
        } else {
            // No log nearby, allow normal decay
            super.randomTick(state, level, pos, random);
        }
        
        // Spread to nearby vanilla leaves - faster than grass and sand
        if (random.nextInt(4) < 3) { // 75% chance per random tick (faster than grass/sand at 50%)
            spreadToNearbyBlocks(level, pos, random);
        }
        
        // Rarely spawn tainted dark saplings below leaves (on ground)
        if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
            spawnSaplingBelow(level, pos, random);
        }
    }
    
    /**
     * Rarely spawns a tainted dark sapling below the leaves (on the ground).
     */
    private void spawnSaplingBelow(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check a few blocks below for a suitable surface
        for (int y = 1; y <= 3; y++) {
            BlockPos checkPos = pos.below(y);
            BlockState checkState = level.getBlockState(checkPos);
            BlockPos aboveCheckPos = checkPos.above();
            BlockState aboveCheckState = level.getBlockState(aboveCheckPos);
            
            // If we find tainted dark grass, dirt, or sand with air above, spawn sapling
            if ((checkState.is(ModBlocks.TAINTED_DARK_GRASS.get()) || 
                 checkState.is(ModBlocks.TAINTED_DARK_DIRT.get()) || 
                 checkState.is(ModBlocks.TAINTED_DARK_SAND.get())) &&
                aboveCheckState.isAir() && 
                level.getMaxLocalRawBrightness(aboveCheckPos) >= 9) {
                // Check if there's already a sapling within 5-6 blocks
                if (!hasSaplingNearby(level, aboveCheckPos, 5)) {
                    level.setBlock(aboveCheckPos, ModBlocks.TAINTED_DARK_SAPLING.get().defaultBlockState(), 3);
                }
                break; // Only spawn one sapling
            }
        }
    }
    
    /**
     * Checks if there's a tainted dark sapling within the specified distance.
     */
    private boolean hasSaplingNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance and is a sapling
                    if (distance < maxDistance && 
                        (level.getBlockState(checkPos).is(ModBlocks.TAINTED_DARK_SAPLING.get()) ||
                         level.getBlockState(checkPos).is(ModBlocks.TAINTED_DARK_TALL_GRASS.get()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Spreads taint to nearby vanilla leaves.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks below and around the leaves
        for (int x = -2; x <= 2; x++) {
            for (int y = -3; y <= 1; y++) { // Check below, same level, and above
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip self
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // Convert vanilla leaves to tainted dark leaves
                    if (checkState.is(Blocks.OAK_LEAVES) || checkState.is(Blocks.BIRCH_LEAVES) ||
                        checkState.is(Blocks.SPRUCE_LEAVES) || checkState.is(Blocks.JUNGLE_LEAVES) ||
                        checkState.is(Blocks.ACACIA_LEAVES) || checkState.is(Blocks.DARK_OAK_LEAVES) ||
                        checkState.is(Blocks.MANGROVE_LEAVES) || checkState.is(Blocks.CHERRY_LEAVES) ||
                        checkState.is(Blocks.AZALEA_LEAVES) || checkState.is(Blocks.FLOWERING_AZALEA_LEAVES) ||
                        checkState.is(Blocks.NETHER_WART_BLOCK) || checkState.is(Blocks.WARPED_WART_BLOCK) ||
                        checkState.is(ModBlocks.TAINTED_DARK_LEAF.get())) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    /**
     * Checks if there's a tainted dark log within the specified range (manhattan distance).
     * Uses a recursive flood-fill-like approach similar to vanilla leaves.
     */
    private boolean hasTaintedDarkLogWithinRange(BlockGetter level, BlockPos pos, int maxDistance) {
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
        
        // Found a tainted dark log!
        if (state.is(ModBlocks.TAINTED_DARK_LOG.get()) || 
            state.is(ModBlocks.STRIPPED_TAINTED_DARK_LOG.get())) {
            return true;
        }
        
        // If we hit a solid block that's not a log or leaf, stop searching
        if (!state.isAir() && 
            !(state.getBlock() instanceof LeavesBlock) &&
            !state.is(ModBlocks.TAINTED_DARK_LOG.get()) &&
            !state.is(ModBlocks.STRIPPED_TAINTED_DARK_LOG.get())) {
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

