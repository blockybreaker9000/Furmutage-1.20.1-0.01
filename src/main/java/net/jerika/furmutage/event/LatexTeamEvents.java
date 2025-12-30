package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexTeamEvents {
    private static final Set<Mob> teamEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    private static final int TARGET_CHECK_INTERVAL = 10; // Check every 10 ticks (0.5 seconds)
    private static final int ATTACK_COOLDOWN = 20; // Attack every 20 ticks (1 second)
    
    /**
     * Check if two entities are on different teams
     */
    private static boolean areOnDifferentTeams(LivingEntity entity1, LivingEntity entity2) {
        if (entity1 == null || entity2 == null || !entity1.isAlive() || !entity2.isAlive()) {
            return false;
        }
        
        String type1 = ForgeRegistries.ENTITY_TYPES.getKey(entity1.getType()).toString();
        String type2 = ForgeRegistries.ENTITY_TYPES.getKey(entity2.getType()).toString();
        
        int team1 = LatexTeamConfig.getTeamForEntity(type1);
        int team2 = LatexTeamConfig.getTeamForEntity(type2);
        
        return team1 != 0 && team2 != 0 && team1 != team2;
    }
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        if (event.getEntity() instanceof Mob mob) {
            String entityType = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).toString();
            
            if (LatexTeamConfig.isEntityInTeam(entityType)) {
                teamEntities.add(mob);
                int team = LatexTeamConfig.getTeamForEntity(entityType);
                String teamName = team == 1 ? "White Latex" : "Dark Latex";
                furmutage.LOGGER.info("[LatexTeamEvents] Registered {} ({})", entityType, teamName);
            }
        }
    }
    
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        
        if (!(event.getEntity() instanceof Mob mob)) {
            return;
        }
        
        String mobType = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).toString();
        if (!LatexTeamConfig.isEntityInTeam(mobType)) {
            return;
        }
        
        if (!mob.isAlive()) {
            teamEntities.remove(mob);
            return;
        }
        
        teamEntities.add(mob);
        
        // Check for targets and attack every N ticks
        if (mob.tickCount % TARGET_CHECK_INTERVAL != 0) {
            return;
        }
        
        LivingEntity currentTarget = mob.getTarget();
        boolean needsNewTarget = (currentTarget == null || !currentTarget.isAlive() || 
                !areOnDifferentTeams(mob, currentTarget));
        
        // Find new target if needed
        if (needsNewTarget) {
            double range = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
            if (range <= 0) {
                range = 32.0;
            }
            
            LivingEntity nearestEnemy = null;
            double nearestDistance = range * range;
            
            for (LivingEntity entity : mob.level().getEntitiesOfClass(
                    LivingEntity.class, 
                    mob.getBoundingBox().inflate(range, range / 2, range))) {
                
                if (!(entity instanceof Mob otherMob) || !otherMob.isAlive() || otherMob == mob) {
                    continue;
                }
                
                String otherType = ForgeRegistries.ENTITY_TYPES.getKey(otherMob.getType()).toString();
                if (!LatexTeamConfig.isEntityInTeam(otherType)) {
                    continue;
                }
                
                if (areOnDifferentTeams(mob, otherMob)) {
                    double distanceSq = mob.distanceToSqr(otherMob);
                    if (distanceSq < nearestDistance) {
                        nearestDistance = distanceSq;
                        nearestEnemy = otherMob;
                    }
                }
            }
            
            if (nearestEnemy != null) {
                mob.setTarget(nearestEnemy);
                mob.setLastHurtByMob(nearestEnemy);
                String enemyType = ForgeRegistries.ENTITY_TYPES.getKey(nearestEnemy.getType()).toString();
                int mobTeam = LatexTeamConfig.getTeamForEntity(mobType);
                int enemyTeam = LatexTeamConfig.getTeamForEntity(enemyType);
                String mobTeamName = mobTeam == 1 ? "White" : "Dark";
                String enemyTeamName = enemyTeam == 1 ? "White" : "Dark";
                furmutage.LOGGER.info("[LatexTeamEvents] {} ({}) -> {} ({}) distance: {}", 
                        mobType, mobTeamName,
                        enemyType, enemyTeamName,
                        String.format("%.1f", mob.distanceTo(nearestEnemy)));
            }
        }
        
        // Attack if target is close enough
        currentTarget = mob.getTarget();
        if (currentTarget != null && currentTarget.isAlive() && 
            areOnDifferentTeams(mob, currentTarget)) {
            
            double distanceSq = mob.distanceToSqr(currentTarget);
            double attackReach = (mob.getBbWidth() + currentTarget.getBbWidth()) * 1.5;
            double attackDistanceSq = attackReach * attackReach;
            
            mob.getLookControl().setLookAt(currentTarget, 30.0F, 30.0F);
            
            if (distanceSq <= attackDistanceSq) {
                // Attack
                if (mob.tickCount % ATTACK_COOLDOWN == 0) {
                    mob.swing(InteractionHand.MAIN_HAND);
                    mob.doHurtTarget(currentTarget);
                    String targetType = ForgeRegistries.ENTITY_TYPES.getKey(currentTarget.getType()).toString();
                    int mobTeam = LatexTeamConfig.getTeamForEntity(mobType);
                    int targetTeam = LatexTeamConfig.getTeamForEntity(targetType);
                    String mobTeamName = mobTeam == 1 ? "White" : "Dark";
                    String targetTeamName = targetTeam == 1 ? "White" : "Dark";
                    furmutage.LOGGER.info("[LatexTeamEvents] ATTACK: {} ({}) -> {} ({})", 
                            mobType, mobTeamName,
                            targetType, targetTeamName);
                }
            } else {
                // Move towards target at slower speed
                if (mob instanceof PathfinderMob pathfinderMob) {
                    // Use slower movement speed (0.5 instead of 1.0)
                    double movementSpeed = mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    if (movementSpeed <= 0) {
                        movementSpeed = 0.25D; // Default slow speed
                    } else {
                        movementSpeed = movementSpeed * 0.5; // Half of normal speed
                    }
                    pathfinderMob.getNavigation().moveTo(currentTarget, movementSpeed);
                }
            }
        }
    }
}

