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
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class TaintedDarkSandBlock extends SandBlock {
    public TaintedDarkSandBlock(int dustColor, Properties properties) {
        super(dustColor, properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread to nearby sand blocks
        if (random.nextInt(12) == 0) { // ~8.3% chance per random tick
            spreadToNearbyBlocks(level, pos, random);
        }
        
        // Rarely spawn dark latex entities on top
        if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
            spawnDarkLatexEntity(level, pos, random);
        }

        // Spawn Changed mod crystals on top (more naturally)
        if (random.nextInt(50) == 0) { // 2% chance per random tick (more frequent spawning)
            spawnChangedCrystalOnTop(level, pos, random);
        }
        
        // Convert nearby water to dark latex fluid
        if (random.nextInt(2) == 0) { // 50% chance per random tick (faster spreading)
            convertNearbyWaterToLatexFluid(level, pos, random, false);
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
            // Check if there's already a sapling within 5-6 blocks
            if (!hasSaplingNearby(level, abovePos, 5)) {
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
                    
                    // Convert sand to tainted dark sand
                    if (checkState.is(Blocks.SAND) || checkState.is(Blocks.RED_SAND)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_SAND.get().defaultBlockState(), 3);
                    }
                    // Convert dirt to tainted dark dirt
                    else if (checkState.is(Blocks.DIRT) || checkState.is(Blocks.COARSE_DIRT)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_DIRT.get().defaultBlockState(), 3);
                    }
                    // Convert grass blocks to tainted dark grass
                    else if (checkState.is(Blocks.GRASS_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_GRASS.get().defaultBlockState(), 3);
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
                }
            }
        }
    }
    
    /**
     * Spawns a dark latex entity (wolf or pup) on top of this block.
     */
    private void spawnDarkLatexEntity(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has enough light
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            // Check if there's already a dark latex entity nearby (within 8 blocks)
            if (!hasDarkLatexNearby(level, abovePos, 8)) {
                // Randomly choose between wolf and pup (50% chance each)
                boolean spawnPup = random.nextBoolean();
                
                EntityType<?> darkLatexType = null;
                
                if (spawnPup) {
                    // Try to find the Dark Latex Pup entity type
                    darkLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:dark_latex_wolf_pup")
                    );
                    
                    // Fallback: try alternative pup names
                    if (darkLatexType == null) {
                        darkLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                                ResourceLocation.tryParse("changed:dark_latex_wolf_male")
                        );
                    }
                } else {
                    // Try to find the Dark Latex Wolf entity type
                    darkLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:dark_latex_wolf_female")
                    );
                }
                
                // Fallback: try to find any entity with "dark_latex" in the name
                if (darkLatexType == null) {
                    for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()) {
                        String name = entityType.getDescriptionId().toLowerCase();
                        if (spawnPup) {
                            if ((name.contains("dark_latex") || name.contains("darklatex")) && 
                                (name.contains("pup") || name.contains("puppy"))) {
                                darkLatexType = entityType;
                                break;
                            }
                        } else {
                            if ((name.contains("dark_latex") || name.contains("darklatex")) && 
                                !name.contains("pup") && !name.contains("puppy")) {
                                darkLatexType = entityType;
                                break;
                            }
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
                            if (name.contains("dark_latex") || name.contains("darklatex")) {
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
    
    /**
     * Converts nearby water blocks to dark latex fluid.
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

