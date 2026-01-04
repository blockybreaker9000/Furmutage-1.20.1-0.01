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

public class TaintedDarkGrassBlock extends GrassBlock {
    public TaintedDarkGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby grass and dirt blocks
        if (random.nextInt(2) == 0) { // 50% chance per random tick (very fast spreading)
            spreadToNearbyBlocks(level, pos, random);
        }
        
        // Spawn tainted dark saplings on top
        if (random.nextInt(10) == 0) { // 10% chance per random tick (more frequent)
            spawnSaplingOnTop(level, pos, random);
        }
        
        // Grow tainted dark saplings and grass
        if (random.nextInt(2) == 0) { // 50% chance per random tick (very fast growth)
            growTaintedVegetation(level, pos, random);
        }
        
        // Rarely spawn dark latex entities on top
        if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
            spawnDarkLatexEntity(level, pos, random);
        }
        
        // Spawn Changed mod crystals on top (more naturally)
        if (random.nextInt(50) == 0) { // 2% chance per random tick (more frequent spawning)
            spawnChangedCrystalOnTop(level, pos, random);
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
                    
                    // Convert grass blocks to tainted dark grass
                    if (checkState.is(Blocks.GRASS_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_GRASS.get().defaultBlockState(), 3);
                    }
                    // Convert dirt to tainted dark dirt
                    else if (checkState.is(Blocks.DIRT) || checkState.is(Blocks.COARSE_DIRT)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_DIRT.get().defaultBlockState(), 3);
                    }
                    // Convert sand to tainted dark sand
                    else if (checkState.is(Blocks.SAND) || checkState.is(Blocks.RED_SAND)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_SAND.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla logs to tainted dark log
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
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla planks to tainted dark planks
                    else if (checkState.is(Blocks.OAK_PLANKS) || checkState.is(Blocks.BIRCH_PLANKS) ||
                        checkState.is(Blocks.SPRUCE_PLANKS) || checkState.is(Blocks.JUNGLE_PLANKS) ||
                        checkState.is(Blocks.ACACIA_PLANKS) || checkState.is(Blocks.DARK_OAK_PLANKS) ||
                        checkState.is(Blocks.MANGROVE_PLANKS) || checkState.is(Blocks.CHERRY_PLANKS) ||
                        checkState.is(Blocks.CRIMSON_PLANKS) || checkState.is(Blocks.WARPED_PLANKS)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_PLANKS.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla leaves to tainted dark leaves
                    else if (checkState.is(Blocks.OAK_LEAVES) || checkState.is(Blocks.BIRCH_LEAVES) ||
                        checkState.is(Blocks.SPRUCE_LEAVES) || checkState.is(Blocks.JUNGLE_LEAVES) ||
                        checkState.is(Blocks.ACACIA_LEAVES) || checkState.is(Blocks.DARK_OAK_LEAVES) ||
                        checkState.is(Blocks.MANGROVE_LEAVES) || checkState.is(Blocks.CHERRY_LEAVES) ||
                        checkState.is(Blocks.AZALEA_LEAVES) || checkState.is(Blocks.FLOWERING_AZALEA_LEAVES) ||
                        checkState.is(Blocks.NETHER_WART_BLOCK) || checkState.is(Blocks.WARPED_WART_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                    }
                    // Also spread to other tainted dark blocks to help them spread further
                    else if (checkState.is(ModBlocks.TAINTED_DARK_DIRT.get())) {
                        // Tainted dark dirt can convert to tainted dark grass if light is available
                        BlockPos aboveCheckPos = checkPos.above();
                        if (level.getBlockState(aboveCheckPos).isAir() && 
                            level.getMaxLocalRawBrightness(aboveCheckPos) >= 9) {
                            level.setBlock(checkPos, ModBlocks.TAINTED_DARK_GRASS.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Rarely spawns a tainted dark sapling on top of this block.
     */
    private void spawnSaplingOnTop(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has enough light
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            // Check if there's already a sapling within 3 blocks
            if (!hasSaplingNearby(level, abovePos, 3)) {
                level.setBlock(abovePos, ModBlocks.TAINTED_DARK_SAPLING.get().defaultBlockState(), 3);
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
     * Grows tainted dark saplings and grass on top of this block.
     */
    private void growTaintedVegetation(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only grow if the space above is air
        if (aboveState.isAir()) {
            if (random.nextInt(3) == 0) {
                // 33% chance to grow a sapling (only if no sapling nearby)
                if (!hasSaplingNearby(level, abovePos, 3)) {
                    level.setBlock(abovePos, ModBlocks.TAINTED_DARK_SAPLING.get().defaultBlockState(), 3);
                }
            } else if (random.nextInt(2) == 0) {
                // 50% chance to grow normal tainted dark grass foliage (faster growth)
                level.setBlock(abovePos, ModBlocks.TAINTED_DARK_GRASS_FOLIAGE.get().defaultBlockState(), 3);
            } else if (random.nextInt(8) == 0) {
                // 12.5% chance to grow tall tainted dark grass (if normal grass didn't grow)
                // Check if there's space for tall grass (needs 2 blocks)
                BlockPos aboveAbovePos = abovePos.above();
                if (level.getBlockState(aboveAbovePos).isAir()) {
                    int variant = random.nextInt(3);
                    level.setBlock(abovePos, ModBlocks.TAINTED_DARK_TALL_GRASS.get().defaultBlockState()
                            .setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                            .setValue(TaintedDarkTallGrassBlock.VARIANT, variant), 3);
                    level.setBlock(aboveAbovePos, ModBlocks.TAINTED_DARK_TALL_GRASS.get().defaultBlockState()
                            .setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                            .setValue(TaintedDarkTallGrassBlock.VARIANT, variant), 3);
                }
            } else if (random.nextInt(4) == 0) {
                // ~6.7% chance to grow a flower (if other vegetation didn't grow)
                // Check if there's already a flower nearby (within 10 blocks)
                if (!hasFlowerNearby(level, abovePos, 10)) {
                    // Randomly choose between roselight and crystal blue flower (50% chance each)
                    if (random.nextBoolean()) {
                        level.setBlock(abovePos, ModBlocks.TAINTED_DARK_ROSELIGHT_FLOWER.get().defaultBlockState(), 3);
                    } else {
                        level.setBlock(abovePos, ModBlocks.TAINTED_DARK_CRYSTAL_BLUE_FLOWER.get().defaultBlockState(), 3);
                    }
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
                        (level.getBlockState(checkPos).is(ModBlocks.TAINTED_DARK_ROSELIGHT_FLOWER.get()) ||
                         level.getBlockState(checkPos).is(ModBlocks.TAINTED_DARK_CRYSTAL_BLUE_FLOWER.get()))) {
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
     * Spawns a tainted dark reed on top of this block if there's water adjacent.
     */

    
    /**
     * Spawns a dark latex entity (wolf male or pup) on top of this block.
     */
    private void spawnDarkLatexEntity(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has enough light
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            // Check if there's already a dark latex entity nearby (within 8 blocks)
            if (!hasDarkLatexNearby(level, abovePos, 8)) {
                EntityType<?> darkLatexType = null;
                
                // Randomly choose between wolf male and pup (50% chance each)
                boolean spawnMale = random.nextBoolean();
                
                if (spawnMale) {
                    // Try to find the Dark Latex Wolf Male entity type
                    darkLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:dark_latex_wolf_male")
                    );
                } else {
                    // Try to find the Dark Latex Wolf Pup entity type
                    darkLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:dark_latex_wolf_pup")
                    );
                }
                
                // Fallback: try to find any entity with matching name
                if (darkLatexType == null) {
                    for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()) {
                        String name = entityType.getDescriptionId().toLowerCase();
                        String key = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString().toLowerCase();
                        
                        if (spawnMale && (name.contains("dark_latex_wolf_male") || key.contains("dark_latex_wolf_male"))) {
                            darkLatexType = entityType;
                            break;
                        } else if (!spawnMale && (name.contains("dark_latex_wolf_pup") || key.contains("dark_latex_wolf_pup"))) {
                            darkLatexType = entityType;
                            break;
                        }
                    }
                }
                
                if (darkLatexType != null && darkLatexType.create(level) instanceof PathfinderMob) {
                    PathfinderMob latexEntity = (PathfinderMob) darkLatexType.create(level);
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
     * Checks if there's a dark latex entity within the specified distance.
     */
    private boolean hasDarkLatexNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance
                    if (distance < maxDistance) {
                        // Check if any dark latex entities exist in the area
                        var entities = level.getEntitiesOfClass(PathfinderMob.class,
                                net.minecraft.world.phys.AABB.ofSize(
                                        net.minecraft.world.phys.Vec3.atCenterOf(checkPos),
                                        1.0, 1.0, 1.0));
                        for (var entity : entities) {
                            String name = entity.getType().getDescriptionId().toLowerCase();
                            String key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString().toLowerCase();
                            if (name.contains("dark_latex_wolf") || key.contains("dark_latex_wolf") ||
                                name.contains("darklatexwolf") || key.contains("darklatexwolf")) {
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
     * Rarely spawns Changed mod crystals in small clusters on top of tainted dark blocks.
     */
    private void spawnChangedCrystalOnTop(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air
        if (!aboveState.isAir()) {
            return;
        }
        
        // Check if there's already a crystal nearby (within 15 blocks)
        if (hasChangedCrystalNearby(level, abovePos, 15)) {
            return;
        }
        
        // Determine cluster size (2-5 crystals)
        int clusterSize = 2 + random.nextInt(4); // 2, 3, 4, or 5 crystals
        
        // Try to get Changed mod crystal blocks using reflection
        try {
            // Get the ChangedBlocks class
            Class<?> changedBlocksClass = Class.forName("net.ltxprogrammer.changed.init.ChangedBlocks");
            
            // List of crystal block names (randomly select one)
            String[] crystalNames = {
                "LATEX_PUP_CRYSTAL",      // Pup crystal
                "LATEX_CRYSTAL",          // Regular dark latex crystal
                "WOLF_CRYSTAL",           // Wolf crystal
                "DARK_LATEX_CRYSTAL_LARGE", // Large dark latex crystal
                "BEIFENG_CRYSTAL",        // Beifeng crystal
                "BEIFENG_CRYSTAL_SMALL",  // Small Beifeng crystal
                "DARK_DRAGON_CRYSTAL"     // Dark dragon crystal
            };
            
            // Shuffle the array to get random order
            java.util.List<String> crystalList = new java.util.ArrayList<>(java.util.Arrays.asList(crystalNames));
            java.util.Collections.shuffle(crystalList, new java.util.Random(random.nextLong()));
            
            // Collect all available crystal blocks
            java.util.List<net.minecraft.world.level.block.Block> availableCrystals = new java.util.ArrayList<>();
            for (String crystalName : crystalList) {
                try {
                    java.lang.reflect.Field field = changedBlocksClass.getField(crystalName);
                    Object registryObject = field.get(null);
                    java.lang.reflect.Method getMethod = registryObject.getClass().getMethod("get");
                    net.minecraft.world.level.block.Block crystalBlock = (net.minecraft.world.level.block.Block) getMethod.invoke(registryObject);
                    if (crystalBlock != null) {
                        availableCrystals.add(crystalBlock);
                    }
                } catch (Exception e) {
                    // Skip this crystal type if it fails
                    continue;
                }
            }
            
            if (availableCrystals.isEmpty()) {
                return; // No crystals available
            }
            
            // Find valid positions in a small radius (2-3 blocks) for the cluster
            java.util.List<BlockPos> validPositions = new java.util.ArrayList<>();
            int clusterRadius = 3;
            
            for (int x = -clusterRadius; x <= clusterRadius; x++) {
                for (int z = -clusterRadius; z <= clusterRadius; z++) {
                    BlockPos checkPos = pos.offset(x, 0, z);
                    BlockPos checkAbovePos = checkPos.above();
                    BlockState checkState = level.getBlockState(checkPos);
                    BlockState checkAboveState = level.getBlockState(checkAbovePos);
                    
                    // Check if the block below is valid (tainted dark grass or sand) and space above is air
                    if ((checkState.is(ModBlocks.TAINTED_DARK_GRASS.get()) || 
                         checkState.is(ModBlocks.TAINTED_DARK_SAND.get())) &&
                        checkAboveState.isAir()) {
                        validPositions.add(checkAbovePos);
                    }
                }
            }
            
            if (validPositions.isEmpty()) {
                return; // No valid positions found
            }
            
            // Shuffle valid positions to randomize placement
            java.util.Collections.shuffle(validPositions, new java.util.Random(random.nextLong()));
            
            // Place crystals in the cluster
            int crystalsPlaced = 0;
            for (int i = 0; i < Math.min(clusterSize, validPositions.size()) && crystalsPlaced < clusterSize; i++) {
                BlockPos crystalPos = validPositions.get(i);
                
                // Randomly select a crystal type for this position
                net.minecraft.world.level.block.Block crystalBlock = availableCrystals.get(random.nextInt(availableCrystals.size()));
                BlockState crystalState = crystalBlock.defaultBlockState();
                boolean isDoubleBlock = crystalState.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF);
                
                if (isDoubleBlock) {
                    // Check if there's space for both halves
                    BlockPos upperPos = crystalPos.above();
                    BlockState upperState = level.getBlockState(upperPos);
                    
                    if (!upperState.isAir()) {
                        continue; // Not enough space, try next position
                    }
                    
                    // Place the lower half
                    BlockState lowerState = crystalState.setValue(
                        net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF,
                        net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER
                    );
                    level.setBlock(crystalPos, lowerState, 3);
                    
                    // Place the upper half
                    BlockState upperCrystalState = crystalState.setValue(
                        net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF,
                        net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER
                    );
                    level.setBlock(upperPos, upperCrystalState, 3);
                } else {
                    // Single block crystal, just place it
                    level.setBlock(crystalPos, crystalState, 3);
                }
                
                crystalsPlaced++;
            }
        } catch (ClassNotFoundException e) {
            // Changed mod not loaded, skip
            furmutage.LOGGER.debug("Changed mod not found, skipping crystal spawn");
        } catch (Exception e) {
            // Log error but don't crash
            furmutage.LOGGER.warn("Failed to spawn Changed mod crystal cluster: {}", e.getMessage());
        }
    }
    
    /**
     * Checks if there's a Changed mod crystal within the specified distance.
     */
    private boolean hasChangedCrystalNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        try {
            // Get the ChangedBlocks class
            Class<?> changedBlocksClass = Class.forName("net.ltxprogrammer.changed.init.ChangedBlocks");
            
            // List of crystal block names to check
            String[] crystalNames = {
                "LATEX_PUP_CRYSTAL",
                "LATEX_CRYSTAL",
                "WOLF_CRYSTAL",
                "DARK_LATEX_CRYSTAL_LARGE",
                "BEIFENG_CRYSTAL",
                "BEIFENG_CRYSTAL_SMALL",
                "DARK_DRAGON_CRYSTAL"
            };
            
            // Collect all crystal blocks
            java.util.Set<net.minecraft.world.level.block.Block> crystalBlocks = new java.util.HashSet<>();
            for (String crystalName : crystalNames) {
                try {
                    java.lang.reflect.Field field = changedBlocksClass.getField(crystalName);
                    Object registryObject = field.get(null);
                    java.lang.reflect.Method getMethod = registryObject.getClass().getMethod("get");
                    net.minecraft.world.level.block.Block crystalBlock = (net.minecraft.world.level.block.Block) getMethod.invoke(registryObject);
                    if (crystalBlock != null) {
                        crystalBlocks.add(crystalBlock);
                    }
                } catch (Exception e) {
                    // Skip this crystal type
                    continue;
                }
            }
            
            // Check if any crystal blocks exist nearby
            int checkRadius = maxDistance;
            for (int x = -checkRadius; x <= checkRadius; x++) {
                for (int y = -checkRadius; y <= checkRadius; y++) {
                    for (int z = -checkRadius; z <= checkRadius; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        
                        BlockPos checkPos = pos.offset(x, y, z);
                        double distance = Math.sqrt(x * x + y * y + z * z);
                        
                        if (distance < maxDistance) {
                            BlockState checkState = level.getBlockState(checkPos);
                            for (net.minecraft.world.level.block.Block crystalBlock : crystalBlocks) {
                                if (checkState.is(crystalBlock)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            // Changed mod not loaded, return false
            return false;
        } catch (Exception e) {
            // Error checking, return false to allow spawning
            return false;
        }
        
        return false;
    }
}

