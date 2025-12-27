package net.jerika.furmutage.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Uncommon vertical lunge attack AI that triggers when the player is 2-4 blocks high above the entity.
 * Only works for furmutage and changed entities.
 */
public class VerticalLungeAttackGoal extends Goal {
    private final PathfinderMob mob;
    private static final double VERTICAL_THRESHOLD_MIN = 2.0D; // Player must be at least 2 blocks higher
    private static final double VERTICAL_THRESHOLD_MAX = 6.5D; // Player must be at most 6 blocks higher (6.5 to include 6.0)
    private static final double HORIZONTAL_DISTANCE_MAX = 4.0D; // Max horizontal distance to trigger
    private static final int COOLDOWN = 100; // 5 seconds cooldown (100 ticks)
    private static final double TRIGGER_CHANCE = 1.00; // 100% chance to trigger when conditions are met
    
    private int cooldownTimer = 0;
    private boolean isLunging = false;
    private int lungeDuration = 0;
    private static final int LUNGE_DURATION = 15; // How long the lunge lasts (15 ticks)

    public VerticalLungeAttackGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Can't use if on cooldown or already lunging
        if (cooldownTimer > 0 || isLunging) {
            return false;
        }

        LivingEntity target = mob.getTarget();
        
        // Must have a target and it must be a player
        if (target == null || !(target instanceof Player)) {
            return false;
        }

        // Must be on ground to lunge
        if (!mob.onGround()) {
            return false;
        }

        // Check if player is 2-4 blocks higher
        double verticalDifference = target.getY() - mob.getY();
        if (verticalDifference < VERTICAL_THRESHOLD_MIN || verticalDifference > VERTICAL_THRESHOLD_MAX) {
            return false;
        }

        // Check horizontal distance (not too far)
        double horizontalDistance = Math.sqrt(
            (target.getX() - mob.getX()) * (target.getX() - mob.getX()) +
            (target.getZ() - mob.getZ()) * (target.getZ() - mob.getZ())
        );
        
        if (horizontalDistance > HORIZONTAL_DISTANCE_MAX) {
            return false;
        }

        // Uncommon trigger - only 15% chance
        return mob.getRandom().nextDouble() < TRIGGER_CHANCE;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue if still lunging
        return isLunging && lungeDuration > 0;
    }

    @Override
    public void start() {
        isLunging = true;
        lungeDuration = LUNGE_DURATION;
        cooldownTimer = COOLDOWN;
        
        LivingEntity target = mob.getTarget();
        if (target != null) {
            // Calculate direction to target
            Vec3 direction = new Vec3(
                target.getX() - mob.getX(),
                target.getY() - mob.getY(),
                target.getZ() - mob.getZ()
            );
            
            // Normalize and scale for lunge
            double distance = direction.length();
            if (distance > 0.1) {
                direction = direction.normalize();
                
                // Strong upward lunge with forward momentum
                double horizontalSpeed = 0.6D; // Forward speed
                double verticalSpeed = 1.0D; // Upward speed (increased for higher lunge)
                
                mob.setDeltaMovement(
                    direction.x * horizontalSpeed,
                    verticalSpeed,
                    direction.z * horizontalSpeed
                );
                
                // Look at target
                mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
        }
    }

    @Override
    public void tick() {
        if (isLunging) {
            lungeDuration--;
            
            LivingEntity target = mob.getTarget();
            if (target != null) {
                // Continue looking at target during lunge
                mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                
                // Try to attack if close enough during lunge
                double distanceSqr = mob.distanceToSqr(target);
                if (distanceSqr < 5.0D * 5.0D) { // Within 5 blocks
                    mob.swing(InteractionHand.MAIN_HAND);
                    mob.doHurtTarget(target);
                }
            }
            
            // End lunge if duration is over or on ground
            if (lungeDuration <= 0 || mob.onGround()) {
                stop();
            }
        }
        
        // Decrement cooldown
        if (cooldownTimer > 0) {
            cooldownTimer--;
        }
    }

    @Override
    public void stop() {
        isLunging = false;
        lungeDuration = 0;
    }
}

