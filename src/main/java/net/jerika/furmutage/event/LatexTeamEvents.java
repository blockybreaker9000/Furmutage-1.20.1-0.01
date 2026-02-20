package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexTeamEvents {
    private static final Set<Mob> teamEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    private static final int TARGET_CHECK_INTERVAL = 20; // Check every 20 ticks (1 second)
    private static final int ATTACK_COOLDOWN = 20; // Attack every 20 ticks (1 second)
    private static final double HORDE_RADIUS = 32.0D; // 32 block radius for horde aggro (like zombie pigmen)
    private static int serverTickCounter = 0;

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
    
    /** Clear static entity set when a level unloads to avoid infinite "Saving world data" hang. */
    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        teamEntities.clear();
    }

    /** Clear on server stop so entities can be released before save. */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        teamEntities.clear();
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        if (event.getEntity() instanceof Mob mob) {
            String entityType = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).toString();
            
            if (LatexTeamConfig.isEntityInTeam(entityType)) {
                if (teamEntities.add(mob)) {
                    int team = LatexTeamConfig.getTeamForEntity(entityType);
                    String teamName = team == 1 ? "White Latex" : "Dark Latex";
                    furmutage.LOGGER.debug("[LatexTeamEvents] Registered {} ({})", entityType, teamName);
                }
            }
        }
    }
    
    /**
     * Process team AI via ServerTickEvent - iterates only tracked team entities
     * instead of receiving LivingTickEvent for every living entity.
     */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (teamEntities.isEmpty()) {
            return;
        }
        serverTickCounter++;

        // Iterate over a snapshot to avoid ConcurrentModificationException when other
        // events add/remove team entities during this tick.
        for (Mob mob : new java.util.ArrayList<>(teamEntities)) {
            if (mob == null || !mob.isAlive()) {
                teamEntities.remove(mob);
                continue;
            }

            String mobType = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).toString();
            if (!LatexTeamConfig.isEntityInTeam(mobType)) {
                teamEntities.remove(mob);
                continue;
            }
            int mobTeam = LatexTeamConfig.getTeamForEntity(mobType);

            // Spread expensive work (target scan + moveTo) across ticks to avoid lag spikes
            int bucket = Math.floorMod(serverTickCounter + mob.getId(), TARGET_CHECK_INTERVAL);
            if (bucket != 0) {
                continue;
            }

            LivingEntity currentTarget = mob.getTarget();

        // Priority 1: Check if entity has an attacker (lastHurtByMob) - prioritize attacker over team targeting
        LivingEntity attacker = mob.getLastHurtByMob();
        double followRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (followRange <= 0) {
            followRange = 42.0;
        }
        double followRangeSq = followRange * followRange;
        
        if (attacker != null && attacker.isAlive() && attacker.distanceToSqr(mob) <= followRangeSq) {
            // If we have an attacker and it's within range, target them instead of team-based targeting
            if (currentTarget != attacker) {
                mob.setTarget(attacker);
                String attackerType = ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType()).toString();
                String mobTeamName = mobTeam == 1 ? "White" : "Dark";
                furmutage.LOGGER.debug("[LatexTeamEvents] {} ({}) -> ATTACKER {} distance: {}", 
                        mobType, mobTeamName,
                        attackerType,
                        String.format("%.1f", mob.distanceTo(attacker)));
            }
        } else {
            // No attacker or attacker is out of range - use team-based targeting
            boolean needsNewTarget = (currentTarget == null || !currentTarget.isAlive() || 
                    !areOnDifferentTeams(mob, currentTarget));
            
            // Find new target if needed
            if (needsNewTarget) {
                double range = followRange;
                
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
                    int enemyTeam = LatexTeamConfig.getTeamForEntity(enemyType);
                    String mobTeamName = mobTeam == 1 ? "White" : "Dark";
                    String enemyTeamName = enemyTeam == 1 ? "White" : "Dark";
                    furmutage.LOGGER.debug("[LatexTeamEvents] {} ({}) -> {} ({}) distance: {}", 
                            mobType, mobTeamName,
                            enemyType, enemyTeamName,
                            String.format("%.1f", mob.distanceTo(nearestEnemy)));
                }
            }
        }
        
        // Attack if target is close enough (either attacker or team enemy)
        currentTarget = mob.getTarget();
        if (currentTarget != null && currentTarget.isAlive()) {
            
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
                    int targetTeam = LatexTeamConfig.getTeamForEntity(targetType);
                    String mobTeamName = mobTeam == 1 ? "White" : "Dark";
                    String targetTeamName = targetTeam == 1 ? "White" : "Dark";
                    furmutage.LOGGER.debug("[LatexTeamEvents] ATTACK: {} ({}) -> {} ({})", 
                            mobType, mobTeamName,
                            targetType, targetTeamName);
                }

            }
        }
        } // end while (it.hasNext())
    }
    
    /**
     * Zombie pigman-style horde behavior: when a team entity is attacked,
     * nearby teammates within 32 blocks also become hostile to the attacker.
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity attackedEntity = event.getEntity();
        
        // Only process on server side
        if (attackedEntity == null || attackedEntity.level().isClientSide) {
            return;
        }
        
        // Check if the attacked entity is in a team
        if (!(attackedEntity instanceof Mob)) {
            return;
        }
        
        String attackedType = ForgeRegistries.ENTITY_TYPES.getKey(attackedEntity.getType()).toString();
        if (!LatexTeamConfig.isEntityInTeam(attackedType)) {
            return;
        }
        
        // Get the attacker
        LivingEntity attacker = null;
        if (event.getSource().getEntity() instanceof LivingEntity livingAttacker) {
            attacker = livingAttacker;
        } else if (event.getSource().getDirectEntity() instanceof LivingEntity directAttacker) {
            attacker = directAttacker;
        }
        
        if (attacker == null || !attacker.isAlive() || attacker == attackedEntity) {
            return;
        }
        
        // Don't aggro or allow damage if attacker is on the same team
        String attackerType = ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType()).toString();
        if (LatexTeamConfig.isEntityInTeam(attackerType)) {
            int attackedTeam = LatexTeamConfig.getTeamForEntity(attackedType);
            int attackerTeam = LatexTeamConfig.getTeamForEntity(attackerType);
            if (attackedTeam == attackerTeam) {
                // Same team: cancel the attack entirely so team members are always passive
                event.setCanceled(true);
                return;
            }
        }
        
        // Find nearby teammates and make them hostile to the attacker
        if (attackedEntity.level() instanceof ServerLevel serverLevel) {
            int attackedTeam = LatexTeamConfig.getTeamForEntity(attackedType);
            double hordeRadiusSq = HORDE_RADIUS * HORDE_RADIUS;
            
            // Search for nearby teammates
            AABB searchArea = attackedEntity.getBoundingBox().inflate(HORDE_RADIUS, HORDE_RADIUS / 2, HORDE_RADIUS);
            for (LivingEntity nearbyEntity : serverLevel.getEntitiesOfClass(
                    LivingEntity.class, searchArea, entity -> {
                        if (!(entity instanceof Mob mob) || !mob.isAlive() || mob == attackedEntity) {
                            return false;
                        }
                        String nearbyType = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
                        if (!LatexTeamConfig.isEntityInTeam(nearbyType)) {
                            return false;
                        }
                        // Only aggro teammates on the same team
                        int nearbyTeam = LatexTeamConfig.getTeamForEntity(nearbyType);
                        return nearbyTeam == attackedTeam;
                    })) {
                
                if (nearbyEntity instanceof Mob nearbyMob) {
                    double distanceSq = attackedEntity.distanceToSqr(nearbyMob);
                    
                    // Check if within horde radius
                    if (distanceSq <= hordeRadiusSq) {
                        // Make the nearby teammate hostile to the attacker
                        // Set the attacker as their target and also as lastHurtByMob
                        nearbyMob.setTarget(attacker);
                        nearbyMob.setLastHurtByMob(attacker);
                    }
                }
            }
        }
    }
}

