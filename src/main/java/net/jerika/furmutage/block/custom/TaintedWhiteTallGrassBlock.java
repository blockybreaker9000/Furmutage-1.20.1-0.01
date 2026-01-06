package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

/**
 * Tall tainted white grass foliage block.
 */
public class TaintedWhiteTallGrassBlock extends DoublePlantBlock {
    public TaintedWhiteTallGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        // Only process spawn logic for the lower half of the double plant
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            // Rarely spawn pure white latex wolf or pup on top
            if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
                spawnPureWhiteLatexWolf(level, pos, random);
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted white blocks
        return state.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_SAND.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_LOG.get()) ||
               state.is(ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_PLANKS.get()) ||
               state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.DIRT) ||
               state.is(Blocks.COARSE_DIRT) ||
               state.is(Blocks.SAND) ||
               state.is(Blocks.RED_SAND);
    }

    /**
     * Spawns a pure white latex wolf or pup on top of this block.
     */
    private void spawnPureWhiteLatexWolf(ServerLevel level, BlockPos pos, RandomSource random) {
        // For tall grass, the above position should be the upper half, so check one more block up
        BlockPos abovePos = pos.above();
        BlockPos spawnPos = abovePos.above();
        BlockState spawnPosState = level.getBlockState(spawnPos);
        
        // Only spawn if the space above the tall grass is air and has enough light
        if (spawnPosState.isAir() && level.getMaxLocalRawBrightness(spawnPos) >= 9) {
            // Check if there's already a pure white latex wolf nearby (within 8 blocks)
            if (!hasPureWhiteLatexWolfNearby(level, spawnPos, 8)) {
                EntityType<?> pureWhiteLatexWolfType = null;
                boolean spawnWolf = random.nextBoolean(); // 50% chance for wolf, 50% for pup
                
                // Randomly choose between wolf and pup (50% chance each)
                if (spawnWolf) {
                    // Try to find the Pure White Latex Wolf entity type
                    pureWhiteLatexWolfType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:pure_white_latex_wolf")
                    );
                } else {
                    // Try to find the Pure White Latex Wolf Pup entity type
                    pureWhiteLatexWolfType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:pure_white_latex_wolf_pup")
                    );
                }
                
                // Fallback: try to find any entity with matching name
                if (pureWhiteLatexWolfType == null) {
                    for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()) {
                        String name = entityType.getDescriptionId().toLowerCase();
                        String key = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString().toLowerCase();
                        
                        if (spawnWolf && ((name.contains("pure_white_latex_wolf") && !name.contains("pup")) || 
                            (key.contains("pure_white_latex_wolf") && !key.contains("pup")))) {
                            pureWhiteLatexWolfType = entityType;
                            break;
                        } else if (!spawnWolf && (name.contains("pure_white_latex_wolf_pup") || key.contains("pure_white_latex_wolf_pup"))) {
                            pureWhiteLatexWolfType = entityType;
                            break;
                        }
                    }
                }
                
                if (pureWhiteLatexWolfType != null && pureWhiteLatexWolfType.create(level) instanceof PathfinderMob) {
                    PathfinderMob latexEntity = (PathfinderMob) pureWhiteLatexWolfType.create(level);
                    if (latexEntity != null) {
                        double spawnX = spawnPos.getX() + 0.5;
                        double spawnY = spawnPos.getY();
                        double spawnZ = spawnPos.getZ() + 0.5;
                        latexEntity.moveTo(spawnX, spawnY, spawnZ, random.nextFloat() * 360.0F, 0.0F);
                        try {
                            latexEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos),
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
     * Checks if there's a pure white latex wolf within the specified distance.
     */
    private boolean hasPureWhiteLatexWolfNearby(ServerLevel level, BlockPos pos, int maxDistance) {
        int checkRadius = maxDistance;
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the spawn position itself
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Check if within distance
                    if (distance < maxDistance) {
                        // Check if any pure white latex wolf entities exist in the area
                        var entities = level.getEntitiesOfClass(PathfinderMob.class,
                                net.minecraft.world.phys.AABB.ofSize(
                                        net.minecraft.world.phys.Vec3.atCenterOf(checkPos),
                                        1.0, 1.0, 1.0));
                        for (var entity : entities) {
                            String name = entity.getType().getDescriptionId().toLowerCase();
                            String key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString().toLowerCase();
                            if (name.contains("pure_white_latex_wolf") || key.contains("pure_white_latex_wolf") ||
                                name.contains("purewhitelatexwolf") || key.contains("purewhitelatexwolf")) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Drop nothing when broken (like vanilla tall grass)
        return Collections.emptyList();
    }
}

