package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.furmutage;
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

        // Rarely spawn pure white latex entities on top
        if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
            spawnPureWhiteLatexEntity(level, pos, random);
        }
        
        // Spawn Changed mod white latex pillars on top (in small clusters)
        if (random.nextInt(10) == 0) { // 10% chance per random tick (frequent)
            spawnWhiteLatexPillarsOnTop(level, pos, random);
        }
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
                    // Convert vanilla planks to tainted white planks
                    else if (checkState.is(Blocks.OAK_PLANKS) || checkState.is(Blocks.BIRCH_PLANKS) ||
                        checkState.is(Blocks.SPRUCE_PLANKS) || checkState.is(Blocks.JUNGLE_PLANKS) ||
                        checkState.is(Blocks.ACACIA_PLANKS) || checkState.is(Blocks.DARK_OAK_PLANKS) ||
                        checkState.is(Blocks.MANGROVE_PLANKS) || checkState.is(Blocks.CHERRY_PLANKS) ||
                        checkState.is(Blocks.CRIMSON_PLANKS) || checkState.is(Blocks.WARPED_PLANKS)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_PLANKS.get().defaultBlockState(), 3);
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
                        try {
                            latexEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(abovePos),
                                    MobSpawnType.EVENT, null, null);
                        } catch (IllegalArgumentException e) {
                            // Catch errors from Changed mod trying to use attributes that don't exist in 1.20.1
                            // (e.g., attack_knockback)
                            if (e.getMessage() != null && e.getMessage().contains("attack_knockback")) {
                                furmutage.LOGGER.debug("Ignoring attack_knockback attribute error from Changed mod: {}", e.getMessage());
                            } else {
                                throw e; // Re-throw if it's a different error
                            }
                        }
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
    
    /**
     * Rarely spawns Changed mod white latex pillars in small clusters on top of tainted white blocks.
     */
    private void spawnWhiteLatexPillarsOnTop(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air
        if (!aboveState.isAir()) {
            return;
        }
        
        // Check if there's already a white latex pillar nearby (within 15 blocks)
        if (hasWhiteLatexPillarNearby(level, abovePos, 15)) {
            return;
        }
        
        // Determine cluster size (2-5 pillars)
        int clusterSize = 2 + random.nextInt(4); // 2, 3, 4, or 5 pillars
        
        // Try to get Changed mod white latex pillar block using reflection
        try {
            // Get the ChangedBlocks class
            Class<?> changedBlocksClass = Class.forName("net.ltxprogrammer.changed.init.ChangedBlocks");
            
            // Get the WHITE_LATEX_PILLAR field
            java.lang.reflect.Field pillarField = changedBlocksClass.getField("WHITE_LATEX_PILLAR");
            Object registryObject = pillarField.get(null);
            java.lang.reflect.Method getMethod = registryObject.getClass().getMethod("get");
            net.minecraft.world.level.block.Block pillarBlock = (net.minecraft.world.level.block.Block) getMethod.invoke(registryObject);
            
            if (pillarBlock == null) {
                return; // Pillar block not available
            }
            
            // Find valid positions in a small radius (2-3 blocks) for the cluster
            java.util.List<BlockPos> validPositions = new java.util.ArrayList<>();
            int clusterRadius = 3;
            
            for (int x = -clusterRadius; x <= clusterRadius; x++) {
                for (int z = -clusterRadius; z <= clusterRadius; z++) {
                    BlockPos checkPos = pos.offset(x, 0, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    BlockPos pillarPos = null;
                    
                    // Check if the block is valid surface for spawning pillars
                    if (checkState.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
                        checkState.is(ModBlocks.TAINTED_WHITE_SAND.get())) {
                        // For grass, sand, or foliage: place pillar directly above
                        BlockPos checkAbovePos = checkPos.above();
                        if (level.getBlockState(checkAbovePos).isAir()) {
                            pillarPos = checkAbovePos;
                        }
                    } else if (checkState.is(ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get())) {
                        // For grass foliage: place pillar directly above
                        BlockPos checkAbovePos = checkPos.above();
                        if (level.getBlockState(checkAbovePos).isAir()) {
                            pillarPos = checkAbovePos;
                        }
                    } else if (checkState.is(ModBlocks.TAINTED_WHITE_TALL_GRASS.get())) {
                        // For tall grass: check if it's upper or lower half
                        if (checkState.hasProperty(DoublePlantBlock.HALF)) {
                            DoubleBlockHalf half = checkState.getValue(DoublePlantBlock.HALF);
                            if (half == DoubleBlockHalf.UPPER) {
                                // Upper half: place pillar directly above
                                BlockPos checkAbovePos = checkPos.above();
                                if (level.getBlockState(checkAbovePos).isAir()) {
                                    pillarPos = checkAbovePos;
                                }
                            } else {
                                // Lower half: check if upper half exists and space above that is air
                                BlockPos upperPos = checkPos.above();
                                BlockState upperState = level.getBlockState(upperPos);
                                if (upperState.is(ModBlocks.TAINTED_WHITE_TALL_GRASS.get()) &&
                                    upperState.hasProperty(DoublePlantBlock.HALF) &&
                                    upperState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                                    BlockPos aboveUpperPos = upperPos.above();
                                    if (level.getBlockState(aboveUpperPos).isAir()) {
                                        pillarPos = aboveUpperPos;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (pillarPos != null) {
                        validPositions.add(pillarPos);
                    }
                }
            }
            
            if (validPositions.isEmpty()) {
                return; // No valid positions found
            }
            
            // Shuffle valid positions to randomize placement
            java.util.Collections.shuffle(validPositions, new java.util.Random(random.nextLong()));
            
            // Place pillars in the cluster
            int pillarsPlaced = 0;
            for (int i = 0; i < Math.min(clusterSize, validPositions.size()) && pillarsPlaced < clusterSize; i++) {
                BlockPos pillarPos = validPositions.get(i);
                
                // Place the pillar
                BlockState pillarState = pillarBlock.defaultBlockState();
                level.setBlock(pillarPos, pillarState, 3);
                
                pillarsPlaced++;
            }
        } catch (ClassNotFoundException e) {
            // Changed mod not loaded, skip
            furmutage.LOGGER.debug("Changed mod not found, skipping white latex pillar spawn");
        } catch (NoSuchFieldException e) {
            // WHITE_LATEX_PILLAR field doesn't exist, skip
            furmutage.LOGGER.debug("WHITE_LATEX_PILLAR field not found in Changed mod: {}", e.getMessage());
        } catch (Exception e) {
            // Log error but don't crash
            furmutage.LOGGER.warn("Failed to spawn Changed mod white latex pillar cluster: {}", e.getMessage());
        }
    }
    
    /**
     * Checks if there's a Changed mod white latex pillar within the specified distance.
     */
    private boolean hasWhiteLatexPillarNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        try {
            // Get the ChangedBlocks class
            Class<?> changedBlocksClass = Class.forName("net.ltxprogrammer.changed.init.ChangedBlocks");
            
            // Get the WHITE_LATEX_PILLAR field
            java.lang.reflect.Field pillarField = changedBlocksClass.getField("WHITE_LATEX_PILLAR");
            Object registryObject = pillarField.get(null);
            java.lang.reflect.Method getMethod = registryObject.getClass().getMethod("get");
            net.minecraft.world.level.block.Block pillarBlock = (net.minecraft.world.level.block.Block) getMethod.invoke(registryObject);
            
            if (pillarBlock == null) {
                return false;
            }
            
            // Check blocks in a radius around the position
            int checkRadius = maxDistance;
            for (int x = -checkRadius; x <= checkRadius; x++) {
                for (int y = -checkRadius; y <= checkRadius; y++) {
                    for (int z = -checkRadius; z <= checkRadius; z++) {
                        if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                        
                        BlockPos checkPos = pos.offset(x, y, z);
                        double distance = Math.sqrt(x * x + y * y + z * z);
                        
                        // Check if within distance and is a white latex pillar
                        if (distance < maxDistance && level.getBlockState(checkPos).is(pillarBlock)) {
                            return true;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            // Changed mod not loaded or field doesn't exist, return false
            return false;
        } catch (Exception e) {
            // Error checking, assume no pillar nearby
            return false;
        }
        
        return false;
    }
}

