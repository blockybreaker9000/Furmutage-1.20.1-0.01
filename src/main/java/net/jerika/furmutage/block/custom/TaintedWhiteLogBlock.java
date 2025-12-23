package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TaintedWhiteLogBlock extends RotatedPillarBlock {
    public TaintedWhiteLogBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby sand, dirt, and grass blocks
        if (random.nextInt(20) == 0) { // 5% chance per random tick
            spreadToNearbyBlocks(level, pos, random);
        }
        
        // Rarely spawn tainted white saplings on top
        if (random.nextInt(250) == 0) { // 0.4% chance per random tick (very rare)
            spawnSaplingOnTop(level, pos, random);
        }
        
        // Occasionally spawn tainted white grass foliage on top
        if (random.nextInt(80) == 0) { // 1.25% chance per random tick
            spawnGrassFoliageOnTop(level, pos, random);
        }
    }
    
    /**
     * Spawns tainted white grass foliage on top of this block.
     */
    private void spawnGrassFoliageOnTop(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has enough light
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            if (random.nextInt(5) == 0) {
                // 20% chance for tall grass
                BlockPos aboveAbovePos = abovePos.above();
                if (level.getBlockState(aboveAbovePos).isAir()) {
                    level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 3);
                    level.setBlock(aboveAbovePos, ModBlocks.TAINTED_WHITE_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 3);
                }
            } else {
                // 80% chance for normal grass
                level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get().defaultBlockState(), 3);
            }
        }
    }
    
    /**
     * Rarely spawns a tainted white sapling on top of this block.
     */
    private void spawnSaplingOnTop(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has enough light
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            // Check if there's already a sapling within 5-6 blocks
            if (!hasSaplingNearby(level, abovePos, 5)) {
                level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_SAPLING.get().defaultBlockState(), 3);
            }
        }
    }
    
    /**
     * Checks if there's a tainted white sapling within the specified distance.
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
                    if (distance < maxDistance && level.getBlockState(checkPos).is(ModBlocks.TAINTED_WHITE_SAPLING.get())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Spreads taint to nearby sand, dirt, and grass blocks.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks around the log (horizontal and below)
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip self
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // Convert grass blocks to tainted white grass
                    if (checkState.is(Blocks.GRASS_BLOCK)) {
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

