package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.LatexTenticleLimbsMutantEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class SpookyFollowPlayerGoal extends Goal {
    private final LatexTenticleLimbsMutantEntity entity;
    private Player targetPlayer;
    private int followTime;
    private int soundCooldown;
    private int teleportCooldown;
    private final double followDistance = 16.0D; // Stay 16 blocks away
    private final double minDistance = 8.0D; // Don't get closer than 8 blocks
    private final double maxDistance = 42.0D; // Don't follow if further than 42 blocks

    public SpookyFollowPlayerGoal(LatexTenticleLimbsMutantEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.entity.getTarget() != null) {
            return false; // Don't follow if already targeting something
        }

        List<Player> players = this.entity.level().getEntitiesOfClass(
                Player.class,
                this.entity.getBoundingBox().inflate(maxDistance, 8.0D, maxDistance),
                player -> player != null && player.isAlive() && !player.isSpectator()
        );

        if (players.isEmpty()) {
            return false;
        }

        // Find the nearest player
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Player player : players) {
            double distance = this.entity.distanceToSqr(player);
            if (distance < nearestDistance && distance >= minDistance * minDistance && distance <= maxDistance * maxDistance) {
                // Check if player can't see us (spooky!)
                if (!this.entity.getSensing().hasLineOfSight(player)) {
                    nearestDistance = distance;
                    nearestPlayer = player;
                }
            }
        }

        if (nearestPlayer == null) {
            // If no player is out of sight, just follow the nearest one anyway
            for (Player player : players) {
                double distance = this.entity.distanceToSqr(player);
                if (distance < nearestDistance && distance >= minDistance * minDistance && distance <= maxDistance * maxDistance) {
                    nearestDistance = distance;
                    nearestPlayer = player;
                }
            }
        }

        this.targetPlayer = nearestPlayer;
        return this.targetPlayer != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetPlayer == null || !this.targetPlayer.isAlive()) {
            return false;
        }

        if (this.entity.getTarget() != null) {
            return false; // Stop following if we have a target
        }

        double distance = this.entity.distanceToSqr(this.targetPlayer);
        return distance >= minDistance * minDistance && distance <= maxDistance * maxDistance;
    }

    @Override
    public void start() {
        this.followTime = 0;
        this.soundCooldown = 0;
        this.teleportCooldown = 0;
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
        this.entity.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetPlayer == null) {
            return;
        }

        this.followTime++;
        this.soundCooldown--;
        this.teleportCooldown--;

        // Look at the player
        this.entity.getLookControl().setLookAt(this.targetPlayer, 30.0F, 30.0F);

        double distance = this.entity.distanceToSqr(this.targetPlayer);
        double desiredDistance = followDistance * followDistance;

        // Move to maintain follow distance
        if (distance > desiredDistance) {
            // Too far, move closer
            Path path = this.entity.getNavigation().createPath(this.targetPlayer, 0);
            if (path != null && path.canReach()) {
                this.entity.getNavigation().moveTo(path, 0.5D);
            } else {
                // Try to move towards player
                this.entity.getNavigation().moveTo(this.targetPlayer, 0.5D);
            }
        } else if (distance < minDistance * minDistance) {
            // Too close, back away
            Vec3 awayFromPlayer = this.entity.position().subtract(this.targetPlayer.position()).normalize().scale(2.0D);
            Vec3 targetPos = this.entity.position().add(awayFromPlayer);
            this.entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 0.5D);
        } else {
            // Good distance, just stop
            this.entity.getNavigation().stop();
        }

        // Play spooky sounds occasionally
        if (this.soundCooldown <= 0 && this.entity.getRandom().nextInt(100) < 3) {
            this.entity.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 0.5F, 0.5F + this.entity.getRandom().nextFloat() * 0.5F);
            this.soundCooldown = 200 + this.entity.getRandom().nextInt(200); // 10-20 seconds
        }

        // Occasionally try to "teleport" behind player (move quickly to a position behind them)
        if (this.teleportCooldown <= 0 && this.entity.getRandom().nextInt(200) < 2 && distance > 12.0D * 12.0D) {
            Vec3 playerPos = this.targetPlayer.position();
            Vec3 playerLook = this.targetPlayer.getLookAngle();
            Vec3 behindPlayer = playerPos.subtract(playerLook.scale(8.0D));
            
            // Try to find a valid position behind the player
            if (this.entity.level().noCollision(this.entity.getType().getAABB(behindPlayer.x, behindPlayer.y, behindPlayer.z))) {
                this.entity.moveTo(behindPlayer.x, behindPlayer.y, behindPlayer.z, this.entity.getYRot(), this.entity.getXRot());
                this.entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.5F, 1.0F);
            }
            this.teleportCooldown = 600 + this.entity.getRandom().nextInt(400); // 30-50 seconds
        }
    }
}

