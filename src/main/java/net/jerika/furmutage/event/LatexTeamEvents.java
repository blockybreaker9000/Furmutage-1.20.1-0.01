package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.config.TransfurTeamHelper;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.damagesource.DamageSource;
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

    /** Effective team for targeting: 0 = human/unknown, 1 = white, 2 = dark. Players use transfur form from Changed. */
    private static int getEffectiveTeam(LivingEntity entity) {
        if (entity == null) return 0;
        if (entity instanceof Player player) return TransfurTeamHelper.getPlayerTransfurTeam(player);
        String type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        return LatexTeamConfig.getTeamForEntity(type);
    }

    /** True only when both are on opposite teams (white vs dark). Not hostile to players or other mobs. */
    private static boolean areOnDifferentTeams(LivingEntity entity1, LivingEntity entity2) {
        if (entity1 == null || entity2 == null || !entity1.isAlive() || !entity2.isAlive()) return false;
        int team1 = getEffectiveTeam(entity1);
        int team2 = getEffectiveTeam(entity2);
        return team1 != 0 && team2 != 0 && team1 != team2 && LatexTeamConfig.areWhiteAndDarkHostile();
    }

    /** True if both have the same non-zero team (e.g. both white, or player transfurred white + white mob). */
    private static boolean isSameTeam(LivingEntity entity1, LivingEntity entity2) {
        if (entity1 == null || entity2 == null) return false;
        int team1 = getEffectiveTeam(entity1);
        int team2 = getEffectiveTeam(entity2);
        return team1 != 0 && team2 != 0 && team1 == team2;
    }

    /**
     * Resolve the living attacker from a damage source so player attacks (melee, bow, trident, etc.)
     * are treated the same: the player is the attacker and latex AI will aggro/target them.
     */
    private static LivingEntity resolveAttacker(DamageSource source) {
        if (source == null) return null;
        var e = source.getEntity();
        if (e instanceof Projectile proj && proj.getOwner() instanceof LivingEntity owner) return owner;
        if (e instanceof LivingEntity living) return living;
        e = source.getDirectEntity();
        if (e instanceof Projectile proj && proj.getOwner() instanceof LivingEntity owner) return owner;
        if (e instanceof LivingEntity living) return living;
        return null;
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

            try {
                processTeamMobTick(mob, mobType, mobTeam);
            } catch (IllegalArgumentException e) {
                // Some mod entities (e.g. Spore UtilityEntity) have broken attribute setup
                // (e.g. "Can't find attribute minecraft:generic.attack_damage"). Skip them to avoid crash.
                teamEntities.remove(mob);
                furmutage.LOGGER.debug("[LatexTeamEvents] Removed {} from team tracking due to attribute error: {}", mobType, e.getMessage());
            }
        }
    }

    /** Per-mob tick logic for team targeting and attacking. Isolated so one bad entity doesn't crash the server. */
    private static void processTeamMobTick(Mob mob, String mobType, int mobTeam) {
            LivingEntity currentTarget = mob.getTarget();
            // Always clear same-team target every tick (e.g. Creeper/bomber sets player target every tick; we must clear it so white-team bomber doesn't attack white-transfurred player)
            if (currentTarget != null && currentTarget.isAlive() && isSameTeam(mob, currentTarget) && !LatexTeamConfig.isSameTeamHostile()) {
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
                currentTarget = null;
            }

            // Spread expensive work (target scan + moveTo) across ticks to avoid lag spikes
            int bucket = Math.floorMod(serverTickCounter + mob.getId(), TARGET_CHECK_INTERVAL);
            if (bucket != 0) {
                return;
            }

            currentTarget = mob.getTarget();
        // Priority 1: Check if entity has an attacker (lastHurtByMob) - prioritize attacker over team targeting
        LivingEntity attacker = mob.getLastHurtByMob();
        double followRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (followRange <= 0) {
            followRange = 42.0;
        }
        double followRangeSq = followRange * followRange;
        
        // Only target attacker if they're on the opposite team OR untransfurred player who attacked (don't target same-team e.g. white-transfurred player vs bomber)
        boolean shouldTargetAttacker = attacker != null && attacker.isAlive() && attacker.distanceToSqr(mob) <= followRangeSq
                && (areOnDifferentTeams(mob, attacker) || (attacker instanceof Player && !isSameTeam(mob, attacker)));
        if (shouldTargetAttacker && attacker != null) {
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
                    if (!entity.isAlive() || entity == mob) continue;
                    if (!areOnDifferentTeams(mob, entity)) continue;
                    double distanceSq = mob.distanceToSqr(entity);
                    if (distanceSq < nearestDistance) {
                        nearestDistance = distanceSq;
                        nearestEnemy = entity;
                    }
                }
                
                if (nearestEnemy != null) {
                    mob.setTarget(nearestEnemy);
                    mob.setLastHurtByMob(nearestEnemy);
                    int enemyTeam = getEffectiveTeam(nearestEnemy);
                    String mobTeamName = mobTeam == 1 ? "White" : "Dark";
                    String enemyTeamName = enemyTeam == 1 ? "White" : (enemyTeam == 2 ? "Dark" : "Human");
                    furmutage.LOGGER.debug("[LatexTeamEvents] {} ({}) -> {} ({}) distance: {}", 
                            mobType, mobTeamName,
                            nearestEnemy instanceof Player ? "player" : ForgeRegistries.ENTITY_TYPES.getKey(nearestEnemy.getType()).toString(),
                            enemyTeamName,
                            String.format("%.1f", mob.distanceTo(nearestEnemy)));
                } else if (currentTarget != null && isSameTeam(mob, currentTarget)) {
                    // Only clear when current target is same team; keep player/other target so not passive when hit
                    mob.setTarget(null);
                    mob.setLastHurtByMob(null);
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
                    int targetTeam = getEffectiveTeam(currentTarget);
                    String mobTeamName = mobTeam == 1 ? "White" : "Dark";
                    String targetTeamName = targetTeam == 1 ? "White" : (targetTeam == 2 ? "Dark" : "Human");
                    furmutage.LOGGER.debug("[LatexTeamEvents] ATTACK: {} ({}) -> {} ({})", 
                            mobType, mobTeamName,
                            currentTarget instanceof Player ? "player" : ForgeRegistries.ENTITY_TYPES.getKey(currentTarget.getType()).toString(),
                            targetTeamName);
                }

            }
        }
    }
    
    /**
     * Zombie pigman-style horde behavior: when a team entity is attacked,
     * nearby teammates within 32 blocks also become hostile to the attacker.
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity attackedEntity = event.getEntity();
        DamageSource source = event.getSource();

        // Only process on server side
        if (attackedEntity == null || attackedEntity.level().isClientSide) {
            return;
        }

        // Never interfere with Changed's own damage to players (including transfur kill logic).
        if (attackedEntity instanceof Player && source != null) {
            try {
                var key = source.typeHolder().unwrapKey().orElse(null);
                if (key != null && "changed".equals(key.location().getNamespace())) {
                    return;
                }
            } catch (Exception ignored) {
            }
        }
        
        // Process when attacked is a team mob or a player (transfur team used for players)
        boolean attackedIsTeamMob = attackedEntity instanceof Mob && LatexTeamConfig.isEntityInTeam(
                ForgeRegistries.ENTITY_TYPES.getKey(attackedEntity.getType()).toString());
        if (!attackedIsTeamMob && !(attackedEntity instanceof Player)) {
            return;
        }
        
        // Get the attacker (resolve projectile owner so player bow/trident/etc. counts as player attacking)
        LivingEntity attacker = resolveAttacker(source);
        
        if (attacker == null || !attacker.isAlive() || attacker == attackedEntity) {
            return;
        }
        
        // Enforce latex_team_relations.json; use effective team (transfur team for players)
        int attackedTeam = getEffectiveTeam(attackedEntity);
        int attackerTeam = getEffectiveTeam(attacker);
        if (attackerTeam != 0 || attackedTeam != 0) {
            if (attackedTeam == attackerTeam && attackedTeam != 0 && !LatexTeamConfig.isSameTeamHostile()) {
                event.setCanceled(true);
                if (attacker instanceof Mob attackerMob && attackerMob.getTarget() == attackedEntity) {
                    attackerMob.setTarget(null);
                    attackerMob.setLastHurtByMob(null);
                }
                return;
            }
            if (attackedTeam != attackerTeam && attackedTeam != 0 && attackerTeam != 0 && !LatexTeamConfig.areWhiteAndDarkHostile()) {
                event.setCanceled(true);
                return;
            }
        }
        
        // Horde aggro: when a team mob is attacked, nearby teammates target the attacker. Allow when attacker is opposite team OR untransfurred (e.g. player with team 0), so pigmen-style group aggro works.
        boolean attackerIsValidForHorde = attackerTeam != attackedTeam || attackerTeam == 0;
        if (attackedEntity.level() instanceof ServerLevel serverLevel && attackedTeam != 0
                && attacker != null && attacker.isAlive() && attackerIsValidForHorde) {
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
                        // Make the nearby teammate hostile to the attacker (opposite team only)
                        nearbyMob.setTarget(attacker);
                        nearbyMob.setLastHurtByMob(attacker);
                    }
                }
            }
        }
    }
}

