package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.registries.ForgeRegistries;

public class TaintedWhiteGrassBlock extends GrassBlock {
    public TaintedWhiteGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby grass and dirt blocks
        if (random.nextInt(2) == 0) { // 50% chance per random tick (very fast spreading)
            spreadToNearbyBlocks(level, pos, random);
        }
        
        // Rarely spawn tainted white saplings on top
        if (random.nextInt(200) == 0) { // 0.5% chance per random tick (very rare)
            spawnSaplingOnTop(level, pos, random);
        }
        
        // Grow tainted white saplings and grass
        if (random.nextInt(2) == 0) { // 50% chance per random tick (very fast growth)
            growTaintedVegetation(level, pos, random);
        }
        
        // Rarely spawn pure white latex entities on top
        if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
            spawnPureWhiteLatexEntity(level, pos, random);
        }
        
        // Occasionally spawn mushrooms on top
        if (random.nextInt(100) == 0) { // 1% chance per random tick
            spawnMushroomOnTop(level, pos, random);
        }
        
        // Spawn tainted white reeds if there's water adjacent
        if (random.nextInt(50) == 0) { // 2% chance per random tick
            spawnReedIfWaterNearby(level, pos, random);
        }
        
        // Spawn Changed mod white latex pillars on top (in small clusters)
        if (random.nextInt(10) == 0) { // 10% chance per random tick (frequent)
            spawnWhiteLatexPillarsOnTop(level, pos, random);
        }
        
        // Spawn thunderium crystal shards on top
        if (random.nextInt(200) == 0) { // 0.5% chance per random tick (decreased from 1%)
            spawnThunderiumCrystalShardsOnTop(level, pos, random);
        }
        
        // Convert nearby water to white latex fluid
        if (random.nextInt(2) == 0) { // 50% chance per random tick (faster spreading)
            convertNearbyWaterToLatexFluid(level, pos, random, true);
        }
    }

    /**
     * Spreads taint to nearby grass and dirt blocks.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks in a 3x3x3 area around this block
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
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
                    // Also spread to other tainted blocks to help them spread further
                    else if (checkState.is(ModBlocks.TAINTED_WHITE_DIRT.get())) {
                        // Tainted dirt can convert to tainted grass if light is available
                        BlockPos aboveCheckPos = checkPos.above();
                        if (level.getBlockState(aboveCheckPos).isAir() && 
                            level.getMaxLocalRawBrightness(aboveCheckPos) >= 9) {
                            level.setBlock(checkPos, ModBlocks.TAINTED_WHITE_GRASS.get().defaultBlockState(), 3);
                        }
                    }
                }
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
     * Grows tainted white saplings and grass on top of this block.
     */
    private void growTaintedVegetation(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only grow if the space above is air
        if (aboveState.isAir()) {
            if (random.nextInt(10) == 0) {
                // 10% chance to grow a sapling (only if no sapling nearby)
                if (!hasSaplingNearby(level, abovePos, 5)) {
                    level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_SAPLING.get().defaultBlockState(), 3);
                }
            } else if (random.nextInt(2) == 0) {
                // 50% chance to grow normal tainted white grass foliage (faster growth)
                level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get().defaultBlockState(), 3);
            } else if (random.nextInt(8) == 0) {
                // 12.5% chance to grow tall tainted white grass (if normal grass didn't grow)
                // Check if there's space for tall grass (needs 2 blocks)
                BlockPos aboveAbovePos = abovePos.above();
                if (level.getBlockState(aboveAbovePos).isAir()) {
                    level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 3);
                    level.setBlock(aboveAbovePos, ModBlocks.TAINTED_WHITE_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 3);
                }
            } else if (random.nextInt(4) == 0) {
                // ~6.7% chance to grow a flower (if other vegetation didn't grow)
                // Check if there's already a flower nearby (within 10 blocks)
                if (!hasFlowerNearby(level, abovePos, 10)) {
                    // Randomly choose between roselight and crystal blue flower (50% chance each)
                    if (random.nextBoolean()) {
                        level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_ROSELIGHT_FLOWER.get().defaultBlockState(), 3);
                    } else {
                        level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_CRYSTAL_BLUE_FLOWER.get().defaultBlockState(), 3);
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
     * Spawns a mushroom (spotted or drip) on top of this block.
     */
    private void spawnMushroomOnTop(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has low light (mushrooms prefer darkness)
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) < 13) {
            // Check if there's already a mushroom nearby (within 4 blocks)
            if (!hasMushroomNearby(level, abovePos, 4)) {
                // Randomly choose between spotted and drip mushroom (50% chance each)
                if (random.nextBoolean()) {
                    level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_SPOTTED_MUSHROOM.get().defaultBlockState(), 3);
                } else {
                    level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_DRIP_MUSHROOM.get().defaultBlockState(), 3);
                }
            }
        }
    }
    
    /**
     * Checks if there's a flower within the specified distance.
     */
    private boolean hasFlowerNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance and is a flower
                    if (distance < maxDistance && 
                        (level.getBlockState(checkPos).is(ModBlocks.TAINTED_WHITE_ROSELIGHT_FLOWER.get()) ||
                         level.getBlockState(checkPos).is(ModBlocks.TAINTED_WHITE_CRYSTAL_BLUE_FLOWER.get()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Checks if there's a mushroom within the specified distance.
     */
    private boolean hasMushroomNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance and is a mushroom
                    if (distance < maxDistance && 
                        (level.getBlockState(checkPos).is(ModBlocks.TAINTED_WHITE_SPOTTED_MUSHROOM.get()) ||
                         level.getBlockState(checkPos).is(ModBlocks.TAINTED_WHITE_DRIP_MUSHROOM.get()))) {
                        return true;
                    }
                }
            }
        }
        return false;
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
     * Spawns a tainted white reed on top of this block if there's water adjacent.
     */
    private void spawnReedIfWaterNearby(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air
        if (!aboveState.isAir()) {
            return;
        }
        
        // Check if there's water adjacent to this grass block (horizontally or below)
        boolean hasWaterNearby = false;
        
        // Check horizontal directions
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos adjacentPos = pos.relative(direction);
            BlockState adjacentState = level.getBlockState(adjacentPos);
            if (adjacentState.is(Blocks.WATER)) {
                hasWaterNearby = true;
                break;
            }
        }
        
        // Also check below (water source block)
        if (!hasWaterNearby) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            if (belowState.is(Blocks.WATER)) {
                hasWaterNearby = true;
            }
        }
        
        // Only spawn if water is nearby and no reed already exists nearby
        if (hasWaterNearby && !hasReedNearby(level, abovePos, 3)) {
            level.setBlock(abovePos, ModBlocks.TAINTED_WHITE_REED.get().defaultBlockState(), 3);
        }
    }
    
    /**
     * Checks if there's a tainted white reed within the specified distance.
     */
    private boolean hasReedNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance and is a reed
                    if (distance < maxDistance && 
                        (level.getBlockState(checkPos).is(ModBlocks.TAINTED_WHITE_REED.get()) ||
                         level.getBlockState(checkPos).is(ModBlocks.TAINTED_WHITE_REED_PLANT.get()))) {
                        return true;
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
    
    /**
     * Spawns thunderium crystal shards on top of tainted white grass blocks.
     */
    private void spawnThunderiumCrystalShardsOnTop(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air
        if (!aboveState.isAir()) {
            return;
        }
        
        // Check if there's already a thunderium crystal shard nearby (within 10 blocks)
        if (hasThunderiumCrystalShardNearby(level, abovePos, 10)) {
            return;
        }
        
        // Spawn the crystal shard
        level.setBlock(abovePos, ModBlocks.THUNDERIUM_CRYSTAL_SHARDS.get().defaultBlockState(), 3);
    }
    
    /**
     * Checks if there's a thunderium crystal shard within the specified distance.
     */
    private boolean hasThunderiumCrystalShardNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance and is a thunderium crystal shard
                    if (distance < maxDistance && level.getBlockState(checkPos).is(ModBlocks.THUNDERIUM_CRYSTAL_SHARDS.get())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Converts nearby water blocks to white latex fluid.
     */
    private void convertNearbyWaterToLatexFluid(ServerLevel level, BlockPos pos, RandomSource random, boolean isWhite) {
        try {
            // Get the ChangedBlocks class
            Class<?> changedBlocksClass = Class.forName("net.ltxprogrammer.changed.init.ChangedBlocks");
            
            // Get the fluid block based on type
            String fluidBlockName = isWhite ? "WHITE_LATEX_FLUID" : "DARK_LATEX_FLUID";
            java.lang.reflect.Field fluidField = changedBlocksClass.getField(fluidBlockName);
            Object fluidRegistryObject = fluidField.get(null);
            
            if (fluidRegistryObject == null) {
                return;
            }
            
            // Call .get() on the RegistryObject to get the actual Block
            java.lang.reflect.Method getMethod = fluidRegistryObject.getClass().getMethod("get");
            net.minecraft.world.level.block.Block latexFluidBlock = (net.minecraft.world.level.block.Block) getMethod.invoke(fluidRegistryObject);
            
            if (latexFluidBlock == null) {
                return;
            }
            
            // Search within 12 blocks for water (increased from 5)
            int radius = 12;
            java.util.List<BlockPos> waterPositions = new java.util.ArrayList<>();
            
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos checkPos = pos.offset(x, y, z);
                        double distance = Math.sqrt(x * x + y * y + z * z);
                        
                        // Only convert water within radius blocks
                        if (distance <= radius) {
                            BlockState checkState = level.getBlockState(checkPos);
                            if (checkState.is(Blocks.WATER)) {
                                waterPositions.add(checkPos);
                            }
                        }
                    }
                }
            }
            
            // Convert up to 10 water blocks per tick (increased from 3 for faster spreading)
            if (!waterPositions.isEmpty()) {
                java.util.Collections.shuffle(waterPositions, new java.util.Random(random.nextLong()));
                int toConvert = Math.min(10, waterPositions.size());
                
                for (int i = 0; i < toConvert; i++) {
                    BlockPos waterPos = waterPositions.get(i);
                    level.setBlock(waterPos, latexFluidBlock.defaultBlockState(), 3);
                }
            }
        } catch (ClassNotFoundException e) {
            // Changed mod not loaded, skip
        } catch (Exception e) {
            furmutage.LOGGER.warn("Failed to convert water to latex fluid: {}", e.getMessage());
        }
    }
}

