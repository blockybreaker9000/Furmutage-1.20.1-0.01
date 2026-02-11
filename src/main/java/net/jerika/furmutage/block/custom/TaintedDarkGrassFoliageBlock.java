package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

/**
 * Normal/short tainted dark grass foliage block.
 */
public class TaintedDarkGrassFoliageBlock extends BushBlock {
    public TaintedDarkGrassFoliageBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        // Rarely spawn dark latex entities (male, female, or pup) on top
        if (random.nextInt(300) == 0) { // ~0.33% chance per random tick (very rare)
            spawnDarkLatexEntity(level, pos, random);
        }
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            // Only apply transfur to players and humanoid entities
            if (canBeTransfurred(livingEntity)) {
                applyDarkLatexTransfur(livingEntity, level);
            }
        }
    }
    
    /**
     * Checks if an entity can be transfurred (only players and humanoid entities).
     */
    private boolean canBeTransfurred(LivingEntity entity) {
        // Only allow players and humanoid mobs
        if (entity instanceof net.minecraft.world.entity.player.Player) {
            return true;
        }
        
        // Check for humanoid mobs that can be transfurred
        net.minecraft.resources.ResourceLocation entityKey = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        boolean isRavager = entityKey != null && entityKey.toString().equals("minecraft:ravager");
        
        boolean isHumanoid = entity instanceof net.minecraft.world.entity.npc.Villager ||
                            entity instanceof net.minecraft.world.entity.monster.Zombie ||
                            entity instanceof net.minecraft.world.entity.monster.Skeleton ||
                            entity instanceof net.minecraft.world.entity.raid.Raider ||
                            isRavager;
        
        if (!isHumanoid) {
            return false;
        }
        
        // Don't transfur entities that are already transfurred or from Changed mod
        String entityClassName = entity.getClass().getName();
        return !entityClassName.startsWith("net.ltxprogrammer.changed.entity");
    }
    
    /**
     * Applies dark latex transfur progress to an entity (copied from dark latex crystal logic).
     */
    private void applyDarkLatexTransfur(LivingEntity entity, Level level) {
        // Apply transfur progress similar to DarkLatexCrystalLarge (4.8f per tick when inside)
        float progressAmount = 4.8f;
        
        try {
            // Use ProcessTransfur.progressTransfur like DarkLatexCrystalLarge does
            Class<?> processTransfurClass = Class.forName("net.ltxprogrammer.changed.process.ProcessTransfur");
            Class<?> transfurVariantClass = Class.forName("net.ltxprogrammer.changed.entity.variant.TransfurVariant");
            Class<?> transfurContextClass = Class.forName("net.ltxprogrammer.changed.entity.TransfurContext");
            Class<?> transfurCauseClass = Class.forName("net.ltxprogrammer.changed.entity.TransfurCause");
            Class<?> changedVariantsClass = Class.forName("net.ltxprogrammer.changed.init.ChangedTransfurVariants");
            
            // Get DARK_LATEX_WOLF_PUP variant (it's a RegistryObject, need to call .get() on it)
            java.lang.reflect.Field darkLatexField = changedVariantsClass.getField("DARK_LATEX_WOLF_PUP");
            Object darkLatexRegistryObject = darkLatexField.get(null);
            
            if (darkLatexRegistryObject == null) {
                return;
            }
            
            // Call .get() on the RegistryObject to get the actual TransfurVariant
            java.lang.reflect.Method getMethod = darkLatexRegistryObject.getClass().getMethod("get");
            Object darkLatexVariant = getMethod.invoke(darkLatexRegistryObject);
            
            if (darkLatexVariant == null) {
                return;
            }
            
            // Get TransfurCause.DARK_LATEX
            java.lang.reflect.Field darkLatexCauseField = transfurCauseClass.getField("DARK_LATEX");
            Object darkLatexCause = darkLatexCauseField.get(null);
            
            if (darkLatexCause == null) {
                return;
            }
            
            // Get TransfurContext.hazard(TransfurCause.DARK_LATEX)
            java.lang.reflect.Method hazardMethod = transfurContextClass.getMethod("hazard", transfurCauseClass);
            Object transfurContext = hazardMethod.invoke(null, darkLatexCause);
            
            if (transfurContext == null) {
                return;
            }
            
            // Call ProcessTransfur.progressTransfur(entity, amount, variant, context)
            java.lang.reflect.Method progressTransfurMethod = processTransfurClass.getMethod(
                "progressTransfur", 
                LivingEntity.class, 
                float.class, 
                transfurVariantClass, 
                transfurContextClass
            );
            
            progressTransfurMethod.invoke(null, entity, progressAmount, darkLatexVariant, transfurContext);
        } catch (Exception e) {
            // Silently fail - Changed mod might not be loaded
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted dark blocks
        return state.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
               state.is(ModBlocks.TAINTED_DARK_LOG.get()) ||
               state.is(ModBlocks.STRIPPED_TAINTED_DARK_LOG.get()) ||
               state.is(ModBlocks.TAINTED_DARK_PLANKS.get()) ||
               state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.DIRT) ||
               state.is(Blocks.COARSE_DIRT) ||
               state.is(Blocks.SAND) ||
               state.is(Blocks.RED_SAND);
    }

    /**
     * Spawns a dark latex entity (wolf male, wolf female, or wolf pup) on top of this block.
     */
    private void spawnDarkLatexEntity(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        // Only spawn if the space above is air and has enough light
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            // Check if there's already a dark latex entity nearby (within 8 blocks)
            if (!hasDarkLatexNearby(level, abovePos, 8)) {
                EntityType<?> darkLatexType = null;
                int variant = random.nextInt(3); // 0 = male, 1 = female, 2 = pup
                
                // Randomly choose between wolf male, wolf female, or wolf pup (33% chance each)
                if (variant == 0) {
                    // Try to find the Dark Latex Wolf Male entity type
                    darkLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:dark_latex_wolf_male")
                    );
                } else if (variant == 1) {
                    // Try to find the Dark Latex Wolf Female entity type
                    darkLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                            ResourceLocation.tryParse("changed:dark_latex_wolf_female")
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
                        
                        if (variant == 0 && (name.contains("dark_latex_wolf_male") || key.contains("dark_latex_wolf_male"))) {
                            darkLatexType = entityType;
                            break;
                        } else if (variant == 1 && (name.contains("dark_latex_wolf_female") || key.contains("dark_latex_wolf_female"))) {
                            darkLatexType = entityType;
                            break;
                        } else if (variant == 2 && (name.contains("dark_latex_wolf_pup") || key.contains("dark_latex_wolf_pup"))) {
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
                            if (name.contains("dark_latex_wolf_male") || key.contains("dark_latex_wolf_female") ||
                                name.contains("dark_latex_wolf_male") || key.contains("dark_latex_wolf_female")) {
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
        // Drop nothing when broken (like vanilla grass)
        return Collections.emptyList();
    }
}

