package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.WitheredLatexPuddingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.pathfinder.Path;

public class PuddingSprintAttackGoal extends MeleeAttackGoal {
    private final WitheredLatexPuddingEntity entity;
    private static final double SLOW_SPEED = 0.5D; // Slow approach speed
    private static final double SPRINT_SPEED = 1.0D; // Fast sprint speed
    private static final double SPRINT_DISTANCE = 8.0D; // Start sprinting when within 8 blocks
    private static final double ATTACK_DISTANCE = 1.0D; // Attack when within 1 block
    private boolean isSprinting = false;
    private int sprintCooldown = 0;
    private static final int SPRINT_DURATION = 40; // Sprint for 2 seconds (40 ticks)
    private static final int SPRINT_COOLDOWN = 500; // Cooldown of 25 seconds (500 ticks)

    public PuddingSprintAttackGoal(PathfinderMob pMob, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, SLOW_SPEED, pFollowingTargetEvenIfNotSeen);
        this.entity = (WitheredLatexPuddingEntity) pMob;
    }

    @Override
    public void start() {
        super.start();
        isSprinting = false;
        sprintCooldown = 0;
        // Reset to slow speed
        this.entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(SLOW_SPEED);
    }

    @Override
    public void stop() {
        super.stop();
        isSprinting = false;
        // Reset to slow speed
        this.entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(SLOW_SPEED);
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        double distanceSqr = this.entity.distanceToSqr(target);
        double sprintDistanceSqr = SPRINT_DISTANCE * SPRINT_DISTANCE;
        double attackDistanceSqr = ATTACK_DISTANCE * ATTACK_DISTANCE;

        // Check if we should start sprinting
        if (!isSprinting && distanceSqr <= sprintDistanceSqr && sprintCooldown <= 0) {
            startSprint();
        }

        // If sprinting, check if sprint duration is over
        if (isSprinting) {
            sprintCooldown--;
            if (sprintCooldown <= 0) {
                endSprint();
            }
        } else if (sprintCooldown > 0) {
            sprintCooldown--;
        }

        // Look at target
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Move towards target with appropriate speed
        if (distanceSqr > attackDistanceSqr) {
            Path path = this.entity.getNavigation().createPath(target, 0);
            if (path != null) {
                this.entity.getNavigation().moveTo(path, isSprinting ? SPRINT_SPEED : SLOW_SPEED);
            } else {
                this.entity.getNavigation().moveTo(target, isSprinting ? SPRINT_SPEED : SLOW_SPEED);
            }
        } else {
            // Close enough to attack
            this.entity.getNavigation().stop();
            this.entity.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            
            // Perform attack
            if (this.entity.getSwingTime() == 0) {
                this.entity.swing(InteractionHand.MAIN_HAND);
                this.entity.doHurtTarget(target);
            }
        }
    }

    private void startSprint() {
        isSprinting = true;
        sprintCooldown = SPRINT_DURATION;
        // Increase speed dramatically
        this.entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(SPRINT_SPEED);
    }

    private void endSprint() {
        isSprinting = false;
        sprintCooldown = SPRINT_COOLDOWN;
        // Return to slow speed
        this.entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(SLOW_SPEED);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        // Attack is handled in tick() method
    }
}

