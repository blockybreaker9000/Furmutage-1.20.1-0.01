package net.jerika.furmutage.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

/**
 * For exoskeleton-type Changed mobs:
 * - Small chance to start following a nearby player from behind
 * - Only active while the player is NOT looking at the mob
 * - Stops immediately once the player looks at it
 */
public class ExoFollowBehindGoal extends Goal {
    private final PathfinderMob mob;
    private Player targetPlayer;
    private final double speedModifier;
    private static final double MIN_RANGE = 4.0D;
    private static final double MAX_RANGE = 16.0D;
    private static final double ACTIVATION_CHANCE = 0.001D; // 0.1% chance per tick when conditions are met

    public ExoFollowBehindGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Only run for exoskeleton-type Changed entities by class name heuristic
        String className = this.mob.getClass().getName().toLowerCase();
        if (!className.contains("exo")) {
            return false;
        }

        // Small random chance
        if (this.mob.getRandom().nextDouble() >= ACTIVATION_CHANCE) {
            return false;
        }

        List<Player> players = this.mob.level().getEntitiesOfClass(
                Player.class,
                this.mob.getBoundingBox().inflate(MAX_RANGE, 4.0D, MAX_RANGE),
                p -> p != null && p.isAlive() && !p.isSpectator()
        );

        if (players.isEmpty()) {
            return false;
        }

        // Pick a random nearby player the mob can path to
        Player candidate = players.get(this.mob.getRandom().nextInt(players.size()));
        double dist = this.mob.distanceTo(candidate);
        if (dist < MIN_RANGE || dist > MAX_RANGE) {
            return false;
        }

        // Only start if the player is NOT currently looking at the mob
        if (isPlayerLookingAtMob(candidate)) {
            return false;
        }

        this.targetPlayer = candidate;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetPlayer == null || !this.targetPlayer.isAlive() || this.targetPlayer.isSpectator()) {
            return false;
        }

        double dist = this.mob.distanceTo(this.targetPlayer);
        if (dist < MIN_RANGE / 2 || dist > MAX_RANGE * 1.5) {
            return false;
        }

        // Stop immediately once the player looks at the mob
        return !isPlayerLookingAtMob(this.targetPlayer);
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetPlayer == null) {
            return;
        }

        // Compute a position slightly behind the player (opposite their look direction)
        Vec3 playerPos = this.targetPlayer.position();
        Vec3 look = this.targetPlayer.getViewVector(1.0F).normalize();
        // 2 blocks behind, slight random offset
        Vec3 behindPos = playerPos.subtract(look.scale(2.0D))
                .add((this.mob.getRandom().nextDouble() - 0.5D) * 0.5D, 0.0D,
                        (this.mob.getRandom().nextDouble() - 0.5D) * 0.5D);

        this.mob.getLookControl().setLookAt(this.targetPlayer, 30.0F, 30.0F);
        this.mob.getNavigation().moveTo(behindPos.x, behindPos.y, behindPos.z, this.speedModifier);
    }

    /**
     * Approximate check if the player is looking at the mob using dot-product between
     * player look direction and vector to mob.
     */
    private boolean isPlayerLookingAtMob(Player player) {
        Vec3 playerLook = player.getViewVector(1.0F).normalize();
        Vec3 toMob = this.mob.position().subtract(player.position()).normalize();
        double dot = playerLook.dot(toMob);
        // cos(35°) ~ 0.82, so > 0.82 means roughly within a 35-degree cone
        return dot > 0.82D && player.hasLineOfSight(this.mob);
    }
}


