package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.furmutage;
import org.slf4j.Logger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.block.Block;
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
        
        // Process players, villagers, pillagers, zombies, and vanilla passive mobs
        boolean isTargetEntity = entity instanceof Player ||
                                 entity instanceof Villager ||
                                 entity instanceof Zombie ||
                                 entity instanceof Raider ||
                                 entity instanceof Cow ||
                                 entity instanceof Pig ||
                                 entity instanceof Chicken ||
                                 entity instanceof Sheep ||
                                 entity instanceof Rabbit ||
                                 entity instanceof Horse;
        
        if (!isTargetEntity) {
            return;
        }
        
        // Check every CHECK_INTERVAL ticks
        if (entity.tickCount % CHECK_INTERVAL != 0) {
            return;
        }
        
        // Check if entity is standing on or in a tainted block
        BlockPos entityPos = entity.blockPosition();
        BlockPos belowPos = entityPos.below();
        BlockPos entityFeetPos = new BlockPos(
            (int) Math.floor(entity.getX()),
            (int) Math.floor(entity.getY()),
            (int) Math.floor(entity.getZ())
        );
        
        // Check the block the entity is standing on
        Block blockBelow = level.getBlockState(belowPos).getBlock();
        Block blockAtFeet = level.getBlockState(entityFeetPos).getBlock();
        
        // Also check blocks around the entity (for leaves that might be touching)
        boolean isOnTaintedBlock = isTaintedBlock(blockBelow) || isTaintedBlock(blockAtFeet);
        
        // Check blocks around entity (for leaves)
        if (!isOnTaintedBlock) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos checkPos = entityFeetPos.offset(x, y, z);
                        Block checkBlock = level.getBlockState(checkPos).getBlock();
                        if (isTaintedBlock(checkBlock)) {
                            isOnTaintedBlock = true;
                            break;
                        }
                    }
                    if (isOnTaintedBlock) break;
                }
                if (isOnTaintedBlock) break;
            }
        }
        
        // Track exposure time and apply transfur if on tainted block
        if (isOnTaintedBlock) {
            // Increment exposure time
            int currentExposure = exposureTime.getOrDefault(entity, 0);
            currentExposure += CHECK_INTERVAL;
            exposureTime.put(entity, currentExposure);
            
            // Apply transfur if exposure time reaches threshold
            if (currentExposure >= TRANSFUR_TIME) {
                if (entity instanceof Player player) {
                    transfurPlayerToPureWhiteLatex(player, level);
                } else if (level instanceof ServerLevel serverLevel) {
                    // Check if it's a vanilla passive mob that should be transformed to infected variant
                    if (entity instanceof Cow && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexCowEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_COW.get());
                    } else if (entity instanceof Pig && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexPigEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_PIG.get());
                    } else if (entity instanceof Chicken && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexChickenEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_CHICKEN.get());
                    } else if (entity instanceof Sheep && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexSheepEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_SHEEP.get());
                    } else if (entity instanceof Rabbit && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexRabbitEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_RABBIT.get());
                    } else if (entity instanceof Horse && !(entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexHorseEntity)) {
                        replaceEntityWithInfectedVariant(entity, serverLevel, ModEntities.WHITE_LATEX_HORSE.get());
                    } else {
                        // For other entities (villagers, pillagers, zombies), use pure white latex
                        replaceEntityWithPureWhiteLatex(entity, serverLevel);
                    }
                }
                exposureTime.remove(entity); // Remove from tracking after transfur
            }
        } else {
            // Not on tainted block, reduce exposure time (decay over time)
            int currentExposure = exposureTime.getOrDefault(entity, 0);
            if (currentExposure > 0) {
                currentExposure = Math.max(0, currentExposure - CHECK_INTERVAL * 2); // Decay twice as fast
                if (currentExposure > 0) {
                    exposureTime.put(entity, currentExposure);
                } else {
                    exposureTime.remove(entity);
                }
            }
        }
    }
    
    /**
     * Checks if a block is a tainted block (wood, leaf, grass, dirt, sand, or foliage).
     */
    private static boolean isTaintedBlock(Block block) {
        return block == ModBlocks.TAINTED_WHITE_LOG.get() ||
               block == ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get() ||
               block == ModBlocks.TAINTED_WHITE_LEAF.get() ||
               block == ModBlocks.TAINTED_WHITE_PLANKS.get() ||
               block == ModBlocks.TAINTED_WHITE_GRASS.get() ||
               block == ModBlocks.TAINTED_WHITE_DIRT.get() ||
               block == ModBlocks.TAINTED_WHITE_SAND.get() ||
               block == ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get() ||
               block == ModBlocks.TAINTED_WHITE_TALL_GRASS.get();
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
            entity instanceof net.jerika.furmutage.entity.custom.WhiteLatexHorseEntity) {
            return true; // Already infected
        }
        
        // For other entities, check if they're from Changed mod (Changed entities are considered transfurred)
        String entityClassName = entity.getClass().getName();
        return entityClassName.startsWith("net.ltxprogrammer.changed.entity");
    }
    
    /**
     * Gets the transfur damage source from Changed mod.
     * Uses reflection to access Changed mod's damage source system.
     */
    private static DamageSource getTransfurDamageSource(Level level, LivingEntity entity) {
        try {
            // Try to get Changed mod's damage sources
            // Changed mod typically has a DamageSources class or uses a specific damage type
            Class<?> changedDamageSourcesClass = Class.forName("net.ltxprogrammer.changed.init.ChangedDamageSources");
            
            // Try to get a static method that returns transfur damage source
            // Common patterns: transfurDamage(Entity), latexDamage(Level, Entity), etc.
            try {
                java.lang.reflect.Method transfurMethod = changedDamageSourcesClass.getMethod("transfurDamage", Level.class, LivingEntity.class);
                return (DamageSource) transfurMethod.invoke(null, level, entity);
            } catch (NoSuchMethodException e1) {
                try {
                    java.lang.reflect.Method latexMethod = changedDamageSourcesClass.getMethod("latexDamage", Level.class, LivingEntity.class);
                    return (DamageSource) latexMethod.invoke(null, level, entity);
                } catch (NoSuchMethodException e2) {
                    // Try getting an instance first
                    try {
                        java.lang.reflect.Method getInstanceMethod = changedDamageSourcesClass.getMethod("instance", Level.class);
                        Object instance = getInstanceMethod.invoke(null, level);
                        if (instance != null) {
                            java.lang.reflect.Method transfurInstanceMethod = changedDamageSourcesClass.getMethod("transfurDamage", LivingEntity.class);
                            return (DamageSource) transfurInstanceMethod.invoke(instance, entity);
                        }
                    } catch (NoSuchMethodException e3) {
                        // Try with just Level
                        try {
                            java.lang.reflect.Method getInstanceMethod = changedDamageSourcesClass.getMethod("of", Level.class);
                            Object instance = getInstanceMethod.invoke(null, level);
                            if (instance != null) {
                                java.lang.reflect.Method transfurInstanceMethod = instance.getClass().getMethod("transfurDamage", LivingEntity.class);
                                return (DamageSource) transfurInstanceMethod.invoke(instance, entity);
                            }
                        } catch (NoSuchMethodException e4) {
                            // Last attempt: try to find any method with "transfur" or "latex" in the name
                            for (java.lang.reflect.Method method : changedDamageSourcesClass.getMethods()) {
                                String methodName = method.getName().toLowerCase();
                                if ((methodName.contains("transfur") || methodName.contains("latex")) && 
                                    DamageSource.class.isAssignableFrom(method.getReturnType())) {
                                    try {
                                        if (method.getParameterCount() == 0) {
                                            return (DamageSource) method.invoke(null);
                                        } else if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == LivingEntity.class) {
                                            return (DamageSource) method.invoke(null, entity);
                                        } else if (method.getParameterCount() == 2 && 
                                                   method.getParameterTypes()[0] == Level.class && 
                                                   method.getParameterTypes()[1] == LivingEntity.class) {
                                            return (DamageSource) method.invoke(null, level, entity);
                                        }
                                    } catch (Exception e) {
                                        // Continue trying other methods
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            // If anything goes wrong, return null to use fallback damage
            return null;
        }
        
        return null;
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
                    infectedEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()),
                            MobSpawnType.EVENT, null, null);
                    
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
     * Transfurs a player into pure white latex using Changed mod API.
     * Uses reflection to access Changed mod's transfur system.
     */
    private static void transfurPlayerToPureWhiteLatex(Player player, Level level) {
        try {
            // Get the LatexVariantInstance for the player
            Class<?> instanceClass = Class.forName("net.ltxprogrammer.changed.entity.LatexVariantInstance");
            java.lang.reflect.Method getMethod = instanceClass.getMethod("get", Player.class);
            Object instance = getMethod.invoke(null, player);
            
            if (instance == null) {
                // Instance doesn't exist, try to create it or use a different method
                return;
            }
            
            // Try to get PureWhiteLatex variant
            Class<?> variantClass = Class.forName("net.ltxprogrammer.changed.init.ChangedVariants");
            java.lang.reflect.Field pureWhiteField = variantClass.getField("PURE_WHITE_LATEX_WOLF");
            Object pureWhiteVariant = pureWhiteField.get(null);
            
            if (pureWhiteVariant != null) {
                // Try to set the variant using the instance
                try {
                    java.lang.reflect.Method setVariantMethod = instanceClass.getMethod("setLatexVariant", Object.class);
                    setVariantMethod.invoke(instance, pureWhiteVariant);
                } catch (NoSuchMethodException e1) {
                    // Try alternative method names
                    try {
                        java.lang.reflect.Method transfurMethod = instanceClass.getMethod("transfur", Object.class);
                        transfurMethod.invoke(instance, pureWhiteVariant);
                    } catch (NoSuchMethodException e2) {
                        // Try with player parameter
                        try {
                            java.lang.reflect.Method transfurPlayerMethod = instanceClass.getMethod("transfur", Player.class, Object.class);
                            transfurPlayerMethod.invoke(null, player, pureWhiteVariant);
                        } catch (NoSuchMethodException e3) {
                            // Try ChangedVariants static method
                            try {
                                java.lang.reflect.Method transfurStaticMethod = variantClass.getMethod("transfur", Player.class, Object.class);
                                transfurStaticMethod.invoke(null, player, pureWhiteVariant);
                            } catch (NoSuchMethodException e4) {
                                // Last attempt: try ChangedTransfurHelper or similar
                                try {
                                    Class<?> helperClass = Class.forName("net.ltxprogrammer.changed.process.ChangedTransfurHelper");
                                    java.lang.reflect.Method transfurHelperMethod = helperClass.getMethod("transfur", Player.class, Object.class);
                                    transfurHelperMethod.invoke(null, player, pureWhiteVariant);
                                } catch (Exception e5) {
                                    // If all methods fail, try to find any transfur method
                                    for (java.lang.reflect.Method method : variantClass.getMethods()) {
                                        if (method.getName().toLowerCase().contains("transfur") && 
                                            method.getParameterCount() >= 1) {
                                            try {
                                                if (method.getParameterCount() == 2 && 
                                                    method.getParameterTypes()[0] == Player.class) {
                                                    method.invoke(null, player, pureWhiteVariant);
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                // Continue trying
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            // If anything goes wrong, log and continue (don't crash)
            furmutage.LOGGER.warn("Failed to transfur player to pure white latex: " + t.getMessage());
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

