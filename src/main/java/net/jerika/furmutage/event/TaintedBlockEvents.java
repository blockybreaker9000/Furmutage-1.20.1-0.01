package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles transfur from tainted wood blocks and leaves to untransfurred entities.
 * Entities will be transfurred into pure white latex over time.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TaintedBlockEvents {
    // Track exposure time for each entity (ticks spent on tainted blocks)
    private static final java.util.Map<LivingEntity, Integer> exposureTime = new java.util.WeakHashMap<>();
    
    // Time required to transfur (in ticks) - 5 seconds = 100 ticks
    private static final int TRANSFUR_TIME = 100;
    
    // Check interval: check every 10 ticks (0.5 seconds)
    private static final int CHECK_INTERVAL = 10;
    
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        
        // Only process on server side
        if (level.isClientSide()) {
            return;
        }
        
        // Skip if entity is dead or invulnerable
        if (!entity.isAlive() || entity.isInvulnerable()) {
            return;
        }
        
        // Check if entity is untransfurred
        if (isTransfurred(entity)) {
            // Remove from tracking if already transfurred
            exposureTime.remove(entity);
            return; // Skip transfurred entities
        }
        
        // Process players, villagers, pillagers, zombies, skeletons, ravagers, and vanilla passive mobs
        // Check for Ravager by entity type ID (since import path may vary)
        boolean isRavager = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()) != null &&
                            ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString().equals("minecraft:ravager");
        
        boolean isTargetEntity = entity instanceof Player ||
                                 entity instanceof Villager ||
                                 entity instanceof Zombie ||
                                 entity instanceof Skeleton ||
                                 entity instanceof Raider ||
                                 isRavager ||
                                 entity instanceof Cow ||
                                 entity instanceof Pig ||
                                 entity instanceof Chicken ||
                                 entity instanceof Sheep ||
                                 entity instanceof Rabbit ||
                                 entity instanceof Horse ||
                                 entity instanceof Squid ||
                                 entity instanceof Llama ||
                                 entity instanceof Dolphin ||
                                 entity instanceof Goat;
        
        if (!isTargetEntity) {
            return;
        }
        
        // Check if entity's bounding box is actually touching/intersecting with a tainted block
        boolean isTouchingTaintedWhiteBlock = false;
        boolean isTouchingTaintedDarkTallGrass = false;
        boolean isTouchingTaintedDarkFoliage = false;
        
        // Track vanilla vines that might need infection
        java.util.List<BlockPos> vanillaVinePositions = new java.util.ArrayList<>();
        
        // Get entity's bounding box
        AABB entityBounds = entity.getBoundingBox();
        
        // Expand slightly to check nearby blocks
        AABB expandedBounds = entityBounds.inflate(0.1);
        
        // Get all block positions that the entity's bounding box overlaps
        int minX = (int) Math.floor(expandedBounds.minX);
        int minY = (int) Math.floor(expandedBounds.minY);
        int minZ = (int) Math.floor(expandedBounds.minZ);
        int maxX = (int) Math.ceil(expandedBounds.maxX);
        int maxY = (int) Math.ceil(expandedBounds.maxY);
        int maxZ = (int) Math.ceil(expandedBounds.maxZ);
        
        // Check each block position that the entity overlaps
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    BlockState blockState = level.getBlockState(checkPos);
                    Block checkBlock = blockState.getBlock();
                    
                    boolean isWhiteBlock = isTaintedWhiteBlock(checkBlock);
                    boolean isDarkTallGrass = checkBlock == ModBlocks.TAINTED_DARK_TALL_GRASS.get();
                    boolean isDarkFoliage = checkBlock == ModBlocks.TAINTED_DARK_GRASS_FOLIAGE.get();
                    boolean isWhiteVine = checkBlock == ModBlocks.TAINTED_WHITE_VINE.get();
                    boolean isDarkVine = checkBlock == ModBlocks.TAINTED_DARK_VINE.get();
                    boolean isVanillaVine = checkBlock == Blocks.VINE;
                    
                    // Track vanilla vines for potential infection
                    if (isVanillaVine) {
                        vanillaVinePositions.add(checkPos);
                    }
                    
                    // Check if touching tainted white vine (vines have no collision, use bounds check)
                    if (isWhiteVine && !isTouchingTaintedWhiteBlock) {
                        AABB blockBounds = new AABB(
                            checkPos.getX(), checkPos.getY(), checkPos.getZ(),
                            checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1
                        );
                        if (entityBounds.intersects(blockBounds)) {
                            isTouchingTaintedWhiteBlock = true;
                        }
                    }
                    
                    // Check if touching tainted dark vine
                    if (isDarkVine) {
                        AABB blockBounds = new AABB(
                            checkPos.getX(), checkPos.getY(), checkPos.getZ(),
                            checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1
                        );
                        if (entityBounds.intersects(blockBounds)) {
                            // Add to dark block tracking - vines can infect like dark foliage
                            if (!isTouchingTaintedDarkFoliage) {
                                isTouchingTaintedDarkFoliage = true;
                            }
                        }
                    }
                    
                    if (isWhiteBlock && !isTouchingTaintedWhiteBlock) {
                        // Check if the block's collision shape intersects with the entity's bounding box
                        VoxelShape blockShape = blockState.getCollisionShape(level, checkPos);
                        
                        boolean intersects = false;
                        
                        // If block has no collision shape, check if entity is inside the block
                        if (blockShape.isEmpty()) {
                            // For blocks without collision (like leaves), check if entity is inside the block bounds
                            AABB blockBounds = new AABB(
                                checkPos.getX(), checkPos.getY(), checkPos.getZ(),
                                checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1
                            );
                            intersects = entityBounds.intersects(blockBounds);
                        } else {
                            // For blocks with collision, check if any part of the collision shape intersects
                            for (AABB blockBox : blockShape.toAabbs()) {
                                AABB offsetBox = blockBox.move(
                                    checkPos.getX(), checkPos.getY(), checkPos.getZ()
                                );
                                if (entityBounds.intersects(offsetBox)) {
                                    intersects = true;
                                    break;
                                }
                            }
                        }
                        
                        if (intersects) {
                            isTouchingTaintedWhiteBlock = true;
                        }
                    }
                    
                    if (isDarkTallGrass && !isTouchingTaintedDarkTallGrass) {
                        // For tall grass (blocks with no collision), check if entity's bounding box overlaps block space
                        // Tall grass blocks have no collision, so entities pass through them
                        // Check if entity's bounding box intersects with the block's bounding box
                        AABB blockBounds = new AABB(
                            checkPos.getX(), checkPos.getY(), checkPos.getZ(),
                            checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1
                        );
                        
                        if (entityBounds.intersects(blockBounds)) {
                            isTouchingTaintedDarkTallGrass = true;
                            furmutage.LOGGER.debug("Entity {} detected touching dark tall grass at {}", entity.getName().getString(), checkPos);
                        }
                    }
                    
                    if (isDarkFoliage && !isTouchingTaintedDarkFoliage) {
                        // For foliage (blocks with no collision), check if entity's bounding box overlaps block space
                        AABB blockBounds = new AABB(
                            checkPos.getX(), checkPos.getY(), checkPos.getZ(),
                            checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1
                        );
                        
                        if (entityBounds.intersects(blockBounds)) {
                            isTouchingTaintedDarkFoliage = true;
                            furmutage.LOGGER.debug("Entity {} detected touching dark foliage at {}", entity.getName().getString(), checkPos);
                        }
                    }
                }
            }
        }
        
        // After checking all blocks, handle vanilla vine infection if we found any
        if (!vanillaVinePositions.isEmpty() && level instanceof ServerLevel serverLevel) {
            boolean hasWhiteBlock = isTouchingTaintedWhiteBlock;
            boolean hasDarkBlock = isTouchingTaintedDarkTallGrass || isTouchingTaintedDarkFoliage;
            
            // Also check for tainted dark vines or other dark blocks in the area
            for (BlockPos vinePos : vanillaVinePositions) {
                // Check surrounding blocks for any tainted dark blocks
                for (Direction dir : Direction.values()) {
                    BlockPos checkPos = vinePos.relative(dir);
                    Block checkBlock = serverLevel.getBlockState(checkPos).getBlock();
                    if (checkBlock == ModBlocks.TAINTED_DARK_VINE.get() ||
                        checkBlock == ModBlocks.TAINTED_DARK_TALL_GRASS.get() ||
                        checkBlock == ModBlocks.TAINTED_DARK_GRASS_FOLIAGE.get() ||
                        checkBlock == ModBlocks.TAINTED_DARK_GRASS.get() ||
                        checkBlock == ModBlocks.TAINTED_DARK_DIRT.get() ||
                        checkBlock == ModBlocks.TAINTED_DARK_SAND.get()) {
                        hasDarkBlock = true;
                        break;
                    }
                }
            }
            
            for (BlockPos vinePos : vanillaVinePositions) {
                infectVineIfNearTaintedBlock(serverLevel, vinePos, hasWhiteBlock, hasDarkBlock);
            }
        }
        
        // Track exposure time and apply transfur progress if touching tainted block (like WhiteLatexPillar)
        // Handle white tainted blocks (pure white latex infection)
        if (isTouchingTaintedWhiteBlock) {
            // Only process untransfurred entities (like WhiteLatexPillar does)
            if (!isTransfurred(entity)) {
                // For vanilla passive mobs, directly replace them with their infected counterparts
                if (level instanceof ServerLevel serverLevel) {
                    boolean wasReplaced = false;
                    
                    // Check each vanilla passive mob type and replace with infected variant
                    if (entity instanceof Cow && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexCowEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_COW.get());
                        wasReplaced = true;
                    } else if (entity instanceof Pig && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexPigEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_PIG.get());
                        wasReplaced = true;
                    } else if (entity instanceof Chicken && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexChickenEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_CHICKEN.get());
                        wasReplaced = true;
                    } else if (entity instanceof Sheep && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexSheepEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_SHEEP.get());
                        wasReplaced = true;
                    } else if (entity instanceof Rabbit && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexRabbitEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_RABBIT.get());
                        wasReplaced = true;
                    } else if (entity instanceof Horse && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexHorseEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_HORSE.get());
                        wasReplaced = true;
                    } else if (entity instanceof Squid && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexSquidEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_SQUID.get());
                        wasReplaced = true;
                    } else if (entity instanceof Llama && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexLlamaEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_LLAMA.get());
                        wasReplaced = true;
                    } else if (entity instanceof Dolphin && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexDolphinEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_DOLPHIN.get());
                        wasReplaced = true;
                    } else if (entity instanceof Goat && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexGoatEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_GOAT.get());
                        wasReplaced = true;
                    }
                    
                    // If entity was replaced, we're done
                    if (wasReplaced) {
                        return;
                    }
                }
                
                // For players and other entities, use ProcessTransfur API (like WhiteLatexPillar does)
                applyWhiteLatexTransfur(entity, level);
            }
        }
        
        // Handle dark tainted tall grass and foliage (dark latex infection)
        boolean isTouchingDarkBlock = isTouchingTaintedDarkTallGrass || isTouchingTaintedDarkFoliage;
        if (isTouchingDarkBlock) {
            // Only process untransfurred entities
            if (!isTransfurred(entity)) {
                // For vanilla passive mobs, directly replace them with their dark latex infected counterparts
                if (level instanceof ServerLevel serverLevel) {
                    boolean wasReplaced = false;
                    
                    // Check each vanilla passive mob type and replace with dark latex infected variant
                    if (entity instanceof Cow && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexCowEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexCowEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_COW.get());
                        wasReplaced = true;
                    } else if (entity instanceof Pig && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexPigEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexPigEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_PIG.get());
                        wasReplaced = true;
                    } else if (entity instanceof Chicken && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexChickenEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexChickenEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_CHICKEN.get());
                        wasReplaced = true;
                    } else if (entity instanceof Sheep && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexSheepEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexSheepEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_SHEEP.get());
                        wasReplaced = true;
                    } else if (entity instanceof Rabbit && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexRabbitEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexRabbitEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_RABBIT.get());
                        wasReplaced = true;
                    } else if (entity instanceof Horse && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexHorseEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexHorseEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_HORSE.get());
                        wasReplaced = true;
                    } else if (entity instanceof Squid && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexSquidEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexSquidEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_SQUID.get());
                        wasReplaced = true;
                    } else if (entity instanceof Llama && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexLlamaEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexLlamaEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_LLAMA.get());
                        wasReplaced = true;
                    } else if (entity instanceof Dolphin && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexDolphinEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexDolphinEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_DOLPHIN.get());
                        wasReplaced = true;
                    } else if (entity instanceof Goat && !(entity instanceof net.jerika.furmutage.entity.custom.DarkLatexGoatEntity) && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexGoatEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.DARK_LATEX_GOAT.get());
                        wasReplaced = true;
                    }
                    
                    // If entity was replaced, we're done
                    if (wasReplaced) {
                        return;
                    }
                }
                
                // For players and other entities, use dark latex transfur API
                furmutage.LOGGER.debug("Entity {} is touching dark foliage/tall grass, applying dark latex transfur", entity.getName().getString());
                applyDarkLatexTransfur(entity, level);
            } else {
                furmutage.LOGGER.debug("Entity {} is touching dark foliage/tall grass but is already transfurred, skipping", entity.getName().getString());
            }
        }
        
        // If not touching any tainted block, clear exposure time
        if (!isTouchingTaintedWhiteBlock && !isTouchingDarkBlock) {
            exposureTime.remove(entity);
        }
    }
    
    /**
     * Fallback method to replace entities when ProcessTransfur API is not available.
     */
    private static void fallbackToEntityReplacement(LivingEntity entity, Level level) {
        if (!(entity instanceof Player) && level instanceof ServerLevel serverLevel) {
            int currentExposure = exposureTime.getOrDefault(entity, 0);
            currentExposure += CHECK_INTERVAL;
            exposureTime.put(entity, currentExposure);
            
            if (currentExposure >= TRANSFUR_TIME) {
                // Check if it's a vanilla passive mob that should be transformed to infected variant
                if (entity instanceof Cow && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexCowEntity)) {
                    replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_COW.get());
                    exposureTime.remove(entity);
                } else if (entity instanceof Pig && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexPigEntity)) {
                    replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_PIG.get());
                    exposureTime.remove(entity);
                } else if (entity instanceof Chicken && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexChickenEntity)) {
                    replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_CHICKEN.get());
                    exposureTime.remove(entity);
                } else if (entity instanceof Sheep && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexSheepEntity)) {
                    replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_SHEEP.get());
                    exposureTime.remove(entity);
                } else if (entity instanceof Rabbit && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexRabbitEntity)) {
                    replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_RABBIT.get());
                    exposureTime.remove(entity);
                } else if (entity instanceof Horse && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexHorseEntity)) {
                    replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_HORSE.get());
                    exposureTime.remove(entity);
                } else {
                    // Check for ravager
                    boolean isRavager = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()) != null &&
                                        ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString().equals("minecraft:ravager");
                    if (isRavager && !(entity instanceof net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.LATEX_MUTANT_FAMILY.get());
                        exposureTime.remove(entity);
                    } else {
                        replaceEntityWithPureWhiteLatex(entity, serverLevel);
                        exposureTime.remove(entity);
                    }
                }
            }
        }
    }
    
    /**
     * Applies white latex transfur progress to an entity (like WhiteLatexPillar does).
     */
    private static void applyWhiteLatexTransfur(LivingEntity entity, Level level) {
        // Apply transfur progress similar to WhiteLatexPillar (4.8f per tick when inside)
        // WhiteLatexPillar applies 1.0f every single tick in entityInside(), so we do the same
        float progressAmount = 1.0f; // Same as WhiteLatexPillar - apply every tick
        
        try {
            // Use ProcessTransfur.progressTransfur like WhiteLatexPillar does
            Class<?> processTransfurClass = Class.forName("net.ltxprogrammer.changed.process.ProcessTransfur");
            Class<?> transfurVariantClass = Class.forName("net.ltxprogrammer.changed.entity.variant.TransfurVariant");
            Class<?> transfurContextClass = Class.forName("net.ltxprogrammer.changed.entity.TransfurContext");
            Class<?> transfurCauseClass = Class.forName("net.ltxprogrammer.changed.entity.TransfurCause");
            Class<?> changedVariantsClass = Class.forName("net.ltxprogrammer.changed.init.ChangedTransfurVariants");
            
            // Get PURE_WHITE_LATEX_WOLF variant (it's a RegistryObject, need to call .get() on it)
            java.lang.reflect.Field pureWhiteField = changedVariantsClass.getField("PURE_WHITE_LATEX_WOLF");
            Object pureWhiteRegistryObject = pureWhiteField.get(null);
            
            if (pureWhiteRegistryObject == null) {
                furmutage.LOGGER.warn("PURE_WHITE_LATEX_WOLF RegistryObject not found in ChangedTransfurVariants");
                fallbackToEntityReplacement(entity, level);
                return;
            }
            
            // Call .get() on the RegistryObject to get the actual TransfurVariant
            java.lang.reflect.Method getMethod = pureWhiteRegistryObject.getClass().getMethod("get");
            Object pureWhiteVariant = getMethod.invoke(pureWhiteRegistryObject);
            
            if (pureWhiteVariant == null) {
                furmutage.LOGGER.warn("PURE_WHITE_LATEX_WOLF.get() returned null - variant may not be registered yet");
                fallbackToEntityReplacement(entity, level);
                return;
            }
            
            // Get TransfurCause.WHITE_LATEX
            java.lang.reflect.Field whiteLatexCauseField = transfurCauseClass.getField("WHITE_LATEX");
            Object whiteLatexCause = whiteLatexCauseField.get(null);
            
            if (whiteLatexCause == null) {
                furmutage.LOGGER.warn("TransfurCause.WHITE_LATEX not found");
                fallbackToEntityReplacement(entity, level);
                return;
            }
            
            // Get TransfurContext.hazard(TransfurCause.WHITE_LATEX)
            java.lang.reflect.Method hazardMethod = transfurContextClass.getMethod("hazard", transfurCauseClass);
            Object transfurContext = hazardMethod.invoke(null, whiteLatexCause);
            
            if (transfurContext == null) {
                furmutage.LOGGER.warn("TransfurContext.hazard() returned null");
                fallbackToEntityReplacement(entity, level);
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
            
            furmutage.LOGGER.debug("Calling ProcessTransfur.progressTransfur for {} with amount {} (white latex)", 
                entity.getName().getString(), progressAmount);
            
            boolean result = (Boolean) progressTransfurMethod.invoke(null, entity, progressAmount, pureWhiteVariant, transfurContext);
            
            if (result) {
                furmutage.LOGGER.info("Successfully transfurred {} to Pure White Latex Wolf", 
                    entity.getName().getString());
            } else {
                furmutage.LOGGER.debug("Applied transfur progress to {}: {} points (not yet transfurred)", 
                    entity.getName().getString(), progressAmount);
            }
        } catch (ClassNotFoundException e) {
            furmutage.LOGGER.warn("Changed mod class not found for transfur progress: {}", e.getMessage());
            // Fall back to entity replacement for non-players
            fallbackToEntityReplacement(entity, level);
        } catch (NoSuchFieldException e) {
            furmutage.LOGGER.warn("Changed mod field not found for transfur progress: {}", e.getMessage());
            fallbackToEntityReplacement(entity, level);
        } catch (NoSuchMethodException e) {
            furmutage.LOGGER.warn("Changed mod method not found for transfur progress: {}", e.getMessage());
            fallbackToEntityReplacement(entity, level);
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                furmutage.LOGGER.warn("Error invoking ProcessTransfur.progressTransfur: {} - {}", 
                    cause.getClass().getSimpleName(), cause.getMessage());
            } else {
                furmutage.LOGGER.warn("Error invoking ProcessTransfur.progressTransfur: {}", e.getMessage());
            }
            fallbackToEntityReplacement(entity, level);
        } catch (Exception e) {
            furmutage.LOGGER.warn("Unexpected error applying transfur progress: {} - {}", 
                e.getClass().getSimpleName(), e.getMessage(), e);
            fallbackToEntityReplacement(entity, level);
        }
    }
    
    /**
     * Applies dark latex transfur progress to an entity (like laser emitter does).
     */
    private static void applyDarkLatexTransfur(LivingEntity entity, Level level) {
        // Apply transfur progress similar to laser emitter (1.0f per tick when inside)
        float progressAmount = 1.0f; // Same as white latex - apply every tick
        
        try {
            // Use ProcessTransfur.progressTransfur like white latex does
            Class<?> processTransfurClass = Class.forName("net.ltxprogrammer.changed.process.ProcessTransfur");
            Class<?> transfurVariantClass = Class.forName("net.ltxprogrammer.changed.entity.variant.TransfurVariant");
            Class<?> transfurContextClass = Class.forName("net.ltxprogrammer.changed.entity.TransfurContext");
            Class<?> transfurCauseClass = Class.forName("net.ltxprogrammer.changed.entity.TransfurCause");
            Class<?> changedVariantsClass = Class.forName("net.ltxprogrammer.changed.init.ChangedTransfurVariants");
            
            // Get DARK_LATEX_WOLF_MALE variant (it's a RegistryObject, need to call .get() on it)
            java.lang.reflect.Field darkLatexField = changedVariantsClass.getField("DARK_LATEX_WOLF_MALE");
            Object darkLatexRegistryObject = darkLatexField.get(null);
            
            if (darkLatexRegistryObject == null) {
                furmutage.LOGGER.warn("DARK_LATEX_WOLF_MALE RegistryObject not found in ChangedTransfurVariants");
                return;
            }
            
            // Call .get() on the RegistryObject to get the actual TransfurVariant
            java.lang.reflect.Method getMethod = darkLatexRegistryObject.getClass().getMethod("get");
            Object darkLatexVariant = getMethod.invoke(darkLatexRegistryObject);
            
            if (darkLatexVariant == null) {
                furmutage.LOGGER.warn("DARK_LATEX_WOLF_MALE.get() returned null - variant may not be registered yet");
                return;
            }
            
            // Get TransfurCause - try common dark latex cause names
            Object darkLatexCause = null;
            String[] causeNames = {"DARK_LATEX", "LATEX", "LATEX_WALL_SPLOTCH", "DARK_LATEX_WOLF"};
            for (String causeName : causeNames) {
                try {
                    java.lang.reflect.Field darkLatexCauseField = transfurCauseClass.getField(causeName);
                    darkLatexCause = darkLatexCauseField.get(null);
                    if (darkLatexCause != null) {
                        furmutage.LOGGER.debug("Found TransfurCause: {}", causeName);
                        break;
                    }
                } catch (NoSuchFieldException e) {
                    // Try next cause name
                    continue;
                }
            }
            
            if (darkLatexCause == null) {
                furmutage.LOGGER.warn("Could not find any valid TransfurCause for dark latex (tried: DARK_LATEX, LATEX, LATEX_WALL_SPLOTCH, DARK_LATEX_WOLF)");
                return;
            }
            
            // Get TransfurContext.hazard(TransfurCause.DARK_LATEX)
            java.lang.reflect.Method hazardMethod = transfurContextClass.getMethod("hazard", transfurCauseClass);
            Object transfurContext = hazardMethod.invoke(null, darkLatexCause);
            
            if (transfurContext == null) {
                furmutage.LOGGER.warn("TransfurContext.hazard() returned null");
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
            
            furmutage.LOGGER.debug("Calling ProcessTransfur.progressTransfur for {} with amount {} (dark latex)", 
                entity.getName().getString(), progressAmount);
            
            boolean result = (Boolean) progressTransfurMethod.invoke(null, entity, progressAmount, darkLatexVariant, transfurContext);
            
            if (result) {
                furmutage.LOGGER.info("Successfully transfurred {} to Dark Latex Wolf Male", 
                    entity.getName().getString());
            } else {
                furmutage.LOGGER.debug("Applied transfur progress to {}: {} points (not yet transfurred)", 
                    entity.getName().getString(), progressAmount);
            }
        } catch (ClassNotFoundException e) {
            furmutage.LOGGER.warn("Changed mod class not found for dark latex transfur progress: {}", e.getMessage());
        } catch (NoSuchFieldException e) {
            furmutage.LOGGER.warn("Changed mod field not found for dark latex transfur progress: {}", e.getMessage());
        } catch (NoSuchMethodException e) {
            furmutage.LOGGER.warn("Changed mod method not found for dark latex transfur progress: {}", e.getMessage());
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                furmutage.LOGGER.warn("Error invoking ProcessTransfur.progressTransfur for dark latex: {} - {}", 
                    cause.getClass().getSimpleName(), cause.getMessage());
            } else {
                furmutage.LOGGER.warn("Error invoking ProcessTransfur.progressTransfur for dark latex: {}", e.getMessage());
            }
        } catch (Exception e) {
            furmutage.LOGGER.warn("Unexpected error applying dark latex transfur progress: {} - {}", 
                e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }
    
    /**
     * Checks if a block is a tainted white block (wood, leaf, grass, dirt, sand, or foliage).
     */
    private static boolean isTaintedWhiteBlock(Block block) {
        return block == ModBlocks.TAINTED_WHITE_LOG.get() ||
               block == ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get() ||
               block == ModBlocks.TAINTED_WHITE_LEAF.get() ||
               block == ModBlocks.TAINTED_WHITE_PLANKS.get() ||
               block == ModBlocks.TAINTED_WHITE_GRASS.get() ||
               block == ModBlocks.TAINTED_WHITE_DIRT.get() ||
               block == ModBlocks.TAINTED_WHITE_SAND.get() ||
               block == ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get() ||
               block == ModBlocks.TAINTED_WHITE_TALL_GRASS.get() ||
               block == ModBlocks.TAINTED_WHITE_VINE.get();
    }
    
    /**
     * Checks if a block is a tainted block (white).
     */
    private static boolean isTaintedBlock(Block block) {
        return isTaintedWhiteBlock(block);
    }
    
    /**
     * Checks if an entity is transfurred.
     * For players, uses the Changed mod API. For other entities, checks if they're from Changed mod.
     */
    private static boolean isTransfurred(LivingEntity entity) {
        // If it's a player, check if they're transfurred
        if (entity instanceof Player player) {
            return isPlayerTransfurred(player);
        }
        
        // Check if entity is already an infected variant
        if (entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexCowEntity ||
            entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexPigEntity ||
            entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexChickenEntity ||
            entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexSheepEntity ||
            entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexRabbitEntity ||
            entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexHorseEntity ||
            entity instanceof net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity) {
            return true; // Already infected
        }
        
        // For other entities, check if they're from Changed mod (Changed entities are considered transfurred)
        String entityClassName = entity.getClass().getName();
        return entityClassName.startsWith("net.ltxprogrammer.changed.entity");
    }
    
    /**
     * Replaces a vanilla passive mob with its infected variant.
     */
    private static void replaceEntityWithInfectedVariant(LivingEntity entity, ServerLevel level, EntityType<? extends LivingEntity> infectedType) {
        try {
            if (infectedType.create(level) instanceof PathfinderMob) {
                PathfinderMob infectedEntity = (PathfinderMob) infectedType.create(level);
                if (infectedEntity != null) {
                    // Copy position and rotation
                    infectedEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
                    
                    // Copy health percentage
                    if (entity.getMaxHealth() > 0) {
                        float healthPercent = entity.getHealth() / entity.getMaxHealth();
                        infectedEntity.setHealth(infectedEntity.getMaxHealth() * healthPercent);
                    }
                    
                    // Finalize spawn
                    try {
                        infectedEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()),
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
                    
                    // Spawn the infected entity
                    level.addFreshEntity(infectedEntity);
                    
                    // Remove the original entity
                    entity.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
                }
            }
        } catch (Throwable t) {
            furmutage.LOGGER.warn("Failed to replace entity with infected variant: " + t.getMessage());
        }
    }
    
    /**
     * Replaces a non-player entity (villager, pillager, zombie) with a pure white latex entity.
     */
    private static void replaceEntityWithPureWhiteLatex(LivingEntity entity, ServerLevel level) {
        try {
            // Find the Pure White Latex entity type
            EntityType<?> pureWhiteLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                    net.minecraft.resources.ResourceLocation.tryParse("changed:pure_white_latex_wolf")
            );
            
            // Fallback: try alternative names
            if (pureWhiteLatexType == null) {
                pureWhiteLatexType = ForgeRegistries.ENTITY_TYPES.getValue(
                        net.minecraft.resources.ResourceLocation.tryParse("changed:pure_white_latex_pup")
                );
            }
            
            if (pureWhiteLatexType == null) {
                // Try to find any entity with "pure_white" in the name
                for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()) {
                    String name = entityType.getDescriptionId().toLowerCase();
                    if (name.contains("pure_white") || name.contains("purewhite")) {
                        pureWhiteLatexType = entityType;
                        break;
                    }
                }
            }
            
            if (pureWhiteLatexType != null && pureWhiteLatexType.create(level) instanceof PathfinderMob) {
                // Spawn the pure white latex entity at the same location
                PathfinderMob latexEntity = (PathfinderMob) pureWhiteLatexType.create(level);
                if (latexEntity != null) {
                    latexEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
                    level.addFreshEntity(latexEntity);
                    
                    // Remove the original entity
                    entity.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
                }
            }
        } catch (Throwable t) {
            furmutage.LOGGER.warn("Failed to replace entity with pure white latex: " + t.getMessage());
        }
    }
    
    /**
     * @deprecated No longer used - transfur is now handled by ProcessTransfur.progressTransfur
     */
    @Deprecated
    private static void transfurPlayerToPureWhiteLatex(Player player, Level level) {
        // Try multiple approaches to transfur the player
        // Approach 1: Try ChangedTransfurHelper first (most common in newer versions)
        try {
            Class<?> helperClass = Class.forName("net.ltxprogrammer.changed.process.ChangedTransfurHelper");
            Class<?> variantClass = Class.forName("net.ltxprogrammer.changed.init.ChangedVariants");
            
            // Try to get PureWhiteLatex variant
            Object pureWhiteVariant = null;
            String[] possibleFieldNames = {"PURE_WHITE_LATEX_WOLF", "PURE_WHITE_LATEX", "PUREWHITE_LATEX_WOLF"};
            for (String fieldName : possibleFieldNames) {
                try {
                    java.lang.reflect.Field pureWhiteField = variantClass.getField(fieldName);
                    pureWhiteVariant = pureWhiteField.get(null);
                    if (pureWhiteVariant != null) {
                        break;
                    }
                } catch (NoSuchFieldException e) {
                    continue;
                }
            }
            
            if (pureWhiteVariant == null) {
                furmutage.LOGGER.warn("Could not find Pure White Latex variant field in ChangedVariants");
                return;
            }
            
            // Try different method signatures on ChangedTransfurHelper
            String[] possibleMethodNames = {"transfur", "transfurPlayer", "transfurTo", "setVariant"};
            for (String methodName : possibleMethodNames) {
                try {
                    // Try (Player, Object) signature
                    try {
                        java.lang.reflect.Method method = helperClass.getMethod(methodName, Player.class, Object.class);
                        method.invoke(null, player, pureWhiteVariant);
                        furmutage.LOGGER.debug("Successfully transfurred player {} using ChangedTransfurHelper.{}", 
                            player.getName().getString(), methodName);
                        return;
                    } catch (NoSuchMethodException e) {
                        // Try (Player, Object, boolean) signature
                        try {
                            java.lang.reflect.Method method = helperClass.getMethod(methodName, Player.class, Object.class, boolean.class);
                            method.invoke(null, player, pureWhiteVariant, false);
                            furmutage.LOGGER.debug("Successfully transfurred player {} using ChangedTransfurHelper.{}(Player, Object, boolean)", 
                                player.getName().getString(), methodName);
                            return;
                        } catch (NoSuchMethodException e2) {
                            continue;
                        }
                    }
                } catch (java.lang.reflect.InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        furmutage.LOGGER.debug("ChangedTransfurHelper.{} failed: {}", methodName, cause.getMessage());
                    }
                    continue;
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (ClassNotFoundException e) {
            // ChangedTransfurHelper doesn't exist, try next approach
        } catch (Exception e) {
            furmutage.LOGGER.debug("Error trying ChangedTransfurHelper: {}", e.getMessage());
        }
        
        // Approach 2: Try ChangedVariants static methods
        try {
            Class<?> variantClass = Class.forName("net.ltxprogrammer.changed.init.ChangedVariants");
            
            // Try to get PureWhiteLatex variant
            Object pureWhiteVariant = null;
            String[] possibleFieldNames = {"PURE_WHITE_LATEX_WOLF", "PURE_WHITE_LATEX", "PUREWHITE_LATEX_WOLF"};
            for (String fieldName : possibleFieldNames) {
                try {
                    java.lang.reflect.Field pureWhiteField = variantClass.getField(fieldName);
                    pureWhiteVariant = pureWhiteField.get(null);
                    if (pureWhiteVariant != null) {
                        break;
                    }
                } catch (NoSuchFieldException e) {
                    continue;
                }
            }
            
            if (pureWhiteVariant == null) {
                furmutage.LOGGER.warn("Could not find Pure White Latex variant field in ChangedVariants");
                return;
            }
            
            // Try to find any transfur method in ChangedVariants
            for (java.lang.reflect.Method method : variantClass.getMethods()) {
                String methodName = method.getName().toLowerCase();
                if (methodName.contains("transfur") && method.getParameterCount() >= 1) {
                    try {
                        if (method.getParameterCount() == 2 && 
                            method.getParameterTypes()[0] == Player.class) {
                            method.invoke(null, player, pureWhiteVariant);
                            furmutage.LOGGER.debug("Successfully transfurred player {} using ChangedVariants.{}", 
                                player.getName().getString(), method.getName());
                            return;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            furmutage.LOGGER.warn("ChangedVariants class not found: {}", e.getMessage());
        } catch (Exception e) {
            furmutage.LOGGER.debug("Error trying ChangedVariants: {}", e.getMessage());
        }
        
        // Approach 3: Try LatexVariantInstance (if it exists in this version)
        try {
            Class<?> instanceClass = Class.forName("net.ltxprogrammer.changed.entity.LatexVariantInstance");
            java.lang.reflect.Method getMethod = instanceClass.getMethod("get", Player.class);
            Object instance = getMethod.invoke(null, player);
            
            if (instance != null) {
                Class<?> variantClass = Class.forName("net.ltxprogrammer.changed.init.ChangedVariants");
                Object pureWhiteVariant = null;
                String[] possibleFieldNames = {"PURE_WHITE_LATEX_WOLF", "PURE_WHITE_LATEX", "PUREWHITE_LATEX_WOLF"};
                for (String fieldName : possibleFieldNames) {
                    try {
                        java.lang.reflect.Field pureWhiteField = variantClass.getField(fieldName);
                        pureWhiteVariant = pureWhiteField.get(null);
                        if (pureWhiteVariant != null) {
                            break;
                        }
                    } catch (NoSuchFieldException e) {
                        continue;
                    }
                }
                
                if (pureWhiteVariant != null) {
                    // Try different methods on the instance
                    String[] methodNames = {"setLatexVariant", "transfur", "transfurTo"};
                    for (String methodName : methodNames) {
                        try {
                            java.lang.reflect.Method method = instanceClass.getMethod(methodName, Object.class);
                            method.invoke(instance, pureWhiteVariant);
                            furmutage.LOGGER.debug("Successfully transfurred player {} using LatexVariantInstance.{}", 
                                player.getName().getString(), methodName);
                            return;
                        } catch (NoSuchMethodException e) {
                            continue;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            // LatexVariantInstance doesn't exist in this version, that's okay
        } catch (Exception e) {
            furmutage.LOGGER.debug("Error trying LatexVariantInstance: {}", e.getMessage());
        }
        
        // Approach 4: Try to find any transfur-related class by scanning common packages
        try {
            // Try to find any class with "transfur" in the name in common Changed mod packages
            String[] packagesToSearch = {
                "net.ltxprogrammer.changed.process",
                "net.ltxprogrammer.changed.init",
                "net.ltxprogrammer.changed.entity",
                "net.ltxprogrammer.changed.util"
            };
            
            for (String packageName : packagesToSearch) {
                try {
                    // Try common class names
                    String[] classNames = {
                        packageName + ".TransfurHelper",
                        packageName + ".ChangedTransfurHelper",
                        packageName + ".TransfurUtil",
                        packageName + ".LatexTransfurHelper"
                    };
                    
                    for (String className : classNames) {
                        try {
                            Class<?> transfurClass = Class.forName(className);
                            // Try to find a static method that takes Player and variant
                            for (java.lang.reflect.Method method : transfurClass.getMethods()) {
                                if (method.getName().toLowerCase().contains("transfur") && 
                                    java.lang.reflect.Modifier.isStatic(method.getModifiers()) &&
                                    method.getParameterCount() >= 1) {
                                    try {
                                        // Try to get the variant first
                                        Class<?> variantClass = Class.forName("net.ltxprogrammer.changed.init.ChangedVariants");
                                        Object pureWhiteVariant = null;
                                        String[] possibleFieldNames = {"PURE_WHITE_LATEX_WOLF", "PURE_WHITE_LATEX", "PUREWHITE_LATEX_WOLF"};
                                        for (String fieldName : possibleFieldNames) {
                                            try {
                                                java.lang.reflect.Field field = variantClass.getField(fieldName);
                                                pureWhiteVariant = field.get(null);
                                                if (pureWhiteVariant != null) break;
                                            } catch (Exception e) {
                                                continue;
                                            }
                                        }
                                        
                                        if (pureWhiteVariant != null && method.getParameterCount() == 2 &&
                                            method.getParameterTypes()[0] == Player.class) {
                                            method.invoke(null, player, pureWhiteVariant);
                                            furmutage.LOGGER.debug("Successfully transfurred player {} using {}.{}", 
                                                player.getName().getString(), className, method.getName());
                                            return;
                                        }
                                    } catch (Exception e) {
                                        continue;
                                    }
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            furmutage.LOGGER.debug("Error searching for transfur classes: {}", e.getMessage());
        }
        
        // If all approaches fail, log a warning
        furmutage.LOGGER.warn("Could not transfur player {} to pure white latex - no compatible API found. " +
            "The Changed mod API may have changed or the required classes are not available.", 
            player.getName().getString());
    }
    
    /**
     * Infects a vanilla vine block if it's near a tainted block.
     */
    private static void infectVineIfNearTaintedBlock(ServerLevel level, BlockPos vinePos, boolean nearWhiteBlock, boolean nearDarkBlock) {
        // Only infect if there's a tainted block adjacent
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = vinePos.relative(direction);
            BlockState adjacentState = level.getBlockState(adjacentPos);
            Block adjacentBlock = adjacentState.getBlock();
            
            // Check if adjacent block is a tainted white block
            if (nearWhiteBlock && isTaintedWhiteBlock(adjacentBlock)) {
                // Replace vanilla vine with tainted white vine, preserving attachment properties
                BlockState vineState = level.getBlockState(vinePos);
                BlockState newVineState = ModBlocks.TAINTED_WHITE_VINE.get().defaultBlockState();
                
                // Copy all directional attachment properties
                for (net.minecraft.world.level.block.state.properties.BooleanProperty property : net.minecraft.world.level.block.VineBlock.PROPERTY_BY_DIRECTION.values()) {
                    if (vineState.hasProperty(property)) {
                        newVineState = newVineState.setValue(property, vineState.getValue(property));
                    }
                }
                
                level.setBlock(vinePos, newVineState, 3);
                return;
            } 
            // Check if adjacent block is a tainted dark block
            else if (nearDarkBlock && (adjacentBlock == ModBlocks.TAINTED_DARK_TALL_GRASS.get() || 
                                         adjacentBlock == ModBlocks.TAINTED_DARK_GRASS_FOLIAGE.get() ||
                                         adjacentBlock == ModBlocks.TAINTED_DARK_VINE.get() ||
                                         adjacentBlock == ModBlocks.TAINTED_DARK_GRASS.get() ||
                                         adjacentBlock == ModBlocks.TAINTED_DARK_DIRT.get() ||
                                         adjacentBlock == ModBlocks.TAINTED_DARK_SAND.get())) {
                // Replace vanilla vine with tainted dark vine, preserving attachment properties
                BlockState vineState = level.getBlockState(vinePos);
                BlockState newVineState = ModBlocks.TAINTED_DARK_VINE.get().defaultBlockState();
                
                // Copy all directional attachment properties
                for (net.minecraft.world.level.block.state.properties.BooleanProperty property : net.minecraft.world.level.block.VineBlock.PROPERTY_BY_DIRECTION.values()) {
                    if (vineState.hasProperty(property)) {
                        newVineState = newVineState.setValue(property, vineState.getValue(property));
                    }
                }
                
                level.setBlock(vinePos, newVineState, 3);
                return;
            }
        }
    }
    
    /**
     * Best-effort check to see if a player is transfurred using the Changed mod API.
     * Uses reflection so this code still compiles even if the Changed classes change.
     */
    private static boolean isPlayerTransfurred(Player player) {
        try {
            Class<?> instanceClass = Class.forName("net.ltxprogrammer.changed.entity.LatexVariantInstance");
            java.lang.reflect.Method getMethod = instanceClass.getMethod("get", Player.class);
            Object instance = getMethod.invoke(null, player);
            if (instance == null) {
                return false;
            }

            // Try to get the latex variant from the instance
            java.lang.reflect.Method getVariantMethod = instanceClass.getMethod("getLatexVariant");
            Object variant = getVariantMethod.invoke(instance);
            return variant != null;
        } catch (Throwable t) {
            // If anything goes wrong (class not found, method missing, etc.),
            // just treat the player as not transfurred to avoid crashes.
            return false;
        }
    }
}

