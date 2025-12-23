package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.registries.ForgeRegistries;

public class TaintedWhiteSandBlock extends SandBlock {
    public TaintedWhiteSandBlock(int dustColor, Properties properties) {
        super(dustColor, properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby sand blocks
        if (random.nextInt(12) == 0) { // ~8.3% chance per random tick
            spreadToNearbyBlocks(level, pos, random);
        }
        
        // Rarely spawn tainted white saplings on top
        if (random.nextInt(200) == 0) { // 0.5% chance per random tick (very rare)
            spawnSaplingOnTop(level, pos, random);
        }
        
        // Occasionally spawn tainted white grass foliage on top
        if (random.nextInt(60) == 0) { // ~1.67% chance per random tick
            spawnGrassFoliageOnTop(level, pos, random);
        }
        
        // Rarely spawn pure white latex entities on top
        if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
            spawnPureWhiteLatexEntity(level, pos, random);
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
            if (random.nextInt(4) == 0) {
                // 25% chance for tall grass
                BlockPos aboveAbovePos = abovePos.above();
                if (level.getBlockState(aboveAbovePos).isAir()) {
                    level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 3);
                    level.setBlock(aboveAbovePos, ModBlocks.TAINTED_WHITE_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 3);
                }
            } else {
                // 75% chance for normal grass
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
     * Spreads taint to nearby sand blocks and can also spread to dirt.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks in a 3x3x3 area around this block
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip self
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // Convert sand to tainted white sand
                    if (checkState.is(Blocks.SAND) || checkState.is(Blocks.RED_SAND)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_SAND.get().defaultBlockState(), 3);
                    }
                    // Convert dirt to tainted white dirt
                    else if (checkState.is(Blocks.DIRT) || checkState.is(Blocks.COARSE_DIRT)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_DIRT.get().defaultBlockState(), 3);
                    }
                    // Convert grass blocks to tainted white grass
                    else if (checkState.is(Blocks.GRASS_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_GRASS.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }
    
    /**
     * Spawns a pure white latex entity (wolf or pup) on top of this block.
     */
    private void spawnPureWhiteLatexEntity(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has enough light
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            // Check if there's already a pure white latex entity nearby (within 8 blocks)
            if (!hasPureWhiteLatexNearby(level, abovePos, 8)) {
                // Randomly choose between wolf and pup (50% chance each)
                boolean spawnPup = random.nextBoolean();
                
                EntityType<?> pureWhiteLatexType = null;
                
                if (spawnPup) {
                    // Try to find the Pure White Latex Pup entity type
                    pureWhiteLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:pure_white_latex_wolf_pup")
                    );
                    
                    // Fallback: try alternative pup names
                    if (pureWhiteLatexType == null) {
                        pureWhiteLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                                ResourceLocation.tryParse("changed:pure_white_latex_pup")
                        );
                    }
                } else {
                    // Try to find the Pure White Latex Wolf entity type
                    pureWhiteLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:pure_white_latex_wolf")
                    );
                }
                
                // Fallback: try to find any entity with "pure_white" in the name
                if (pureWhiteLatexType == null) {
                    for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()) {
                        String name = entityType.getDescriptionId().toLowerCase();
                        if (spawnPup) {
                            if ((name.contains("pure_white") || name.contains("purewhite")) && 
                                (name.contains("pup") || name.contains("puppy"))) {
                                pureWhiteLatexType = entityType;
                                break;
                            }
                        } else {
                            if ((name.contains("pure_white") || name.contains("purewhite")) && 
                                !name.contains("pup") && !name.contains("puppy")) {
                                pureWhiteLatexType = entityType;
                                break;
                            }
                        }
                    }
                }
                
                if (pureWhiteLatexType != null && pureWhiteLatexType.create(level) instanceof PathfinderMob) {
                    PathfinderMob latexEntity = (PathfinderMob) pureWhiteLatexType.create(level);
                    if (latexEntity != null) {
                        double spawnX = abovePos.getX() + 0.5;
                        double spawnY = abovePos.getY();
                        double spawnZ = abovePos.getZ() + 0.5;
                        latexEntity.moveTo(spawnX, spawnY, spawnZ, random.nextFloat() * 360.0F, 0.0F);
                        latexEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(abovePos),
                                MobSpawnType.EVENT, null, null);
                        level.addFreshEntity(latexEntity);
                    }
                }
            }
        }
    }
    
    /**
     * Checks if there's a pure white latex entity within the specified distance.
     */
    private boolean hasPureWhiteLatexNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance
                    if (distance < maxDistance) {
                        // Check if any pure white latex entities exist in the area
                        var entities = level.getEntitiesOfClass(PathfinderMob.class,
                                net.minecraft.world.phys.AABB.ofSize(
                                        net.minecraft.world.phys.Vec3.atCenterOf(checkPos),
                                        1.0, 1.0, 1.0));
                        for (var entity : entities) {
                            String name = entity.getType().getDescriptionId().toLowerCase();
                            if (name.contains("pure_white") || name.contains("purewhite")) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

