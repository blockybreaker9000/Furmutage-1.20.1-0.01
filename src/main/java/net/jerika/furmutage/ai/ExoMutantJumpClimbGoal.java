package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.LatexExoMutantEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.Vec3;

public class ExoMutantJumpClimbGoal extends MeleeAttackGoal {
    private final LatexExoMutantEntity entity;
    private final double speedModifier;
    private static final double LUNGE_DISTANCE = 12.0D; // Diagonal lunge when target is 12+ blocks away
    private static final double JUMP_DISTANCE = 6.0D; // Jump when within 6 blocks (high jump)
    private static final double HIGH_JUMP_DISTANCE = 8.0D; // High jump when within 8 blocks
    private static final double ATTACK_DISTANCE = 2.0D; // Attack when within 2 blocks
    private static final double WALL_CLIMB_DISTANCE = 4.0D; // Try to climb wall when within 4 blocks
    private int lungeCooldown = 0;
    private static final int LUNGE_COOLDOWN = 40; // Cooldown of 2 seconds (40 ticks)
    private boolean isLunging = false;
    private int jumpCooldown = 0;
    private static final int JUMP_COOLDOWN = 20; // Cooldown of 1 second (20 ticks)
    private boolean isJumping = false;
    private int attackCooldown = 0;
    private static final int ATTACK_COOLDOWN = 20; // Attack cooldown of 1 second (20 ticks)

    public ExoMutantJumpClimbGoal(LatexExoMutantEntity entity, double speedModifier, boolean followTargetEvenIfNotSeen) {
        super(entity, speedModifier, followTargetEvenIfNotSeen);
        this.entity = entity;
        this.speedModifier = speedModifier;
    }

    @Override
    public void start() {
        super.start();
        lungeCooldown = 0;
        jumpCooldown = 0;
        isLunging = false;
        isJumping = false;
    }

    @Override
    public void stop() {
        super.stop();
        isLunging = false;
        isJumping = false;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        // Reset jumping/lunging state when on ground and not actively jumping
        if (this.entity.onGround() && (isJumping || isLunging) && jumpCooldown <= 10 && lungeCooldown <= 10) {
            this.entity.setJumping(false);
            isJumping = false;
            isLunging = false;
        }

        double distanceSqr = this.entity.distanceToSqr(target);
        double lungeDistanceSqr = LUNGE_DISTANCE * LUNGE_DISTANCE;
        double jumpDistanceSqr = JUMP_DISTANCE * JUMP_DISTANCE;
        double highJumpDistanceSqr = HIGH_JUMP_DISTANCE * HIGH_JUMP_DISTANCE;
        double attackDistanceSqr = ATTACK_DISTANCE * ATTACK_DISTANCE;
        double wallClimbDistanceSqr = WALL_CLIMB_DISTANCE * WALL_CLIMB_DISTANCE;

        // Look at target
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Check for diagonal lunge when target is far away
        if (lungeCooldown <= 0 && this.entity.onGround() && distanceSqr > lungeDistanceSqr) {
            performDiagonalLunge(target);
        }

        // Check if target is above us and we should climb
        double heightDiff = target.getY() - this.entity.getY();
        boolean targetIsAbove = heightDiff > 2.0D;

        // Check if we can't reach target normally (wall in the way or target is high up)
        if (targetIsAbove && distanceSqr <= wallClimbDistanceSqr && this.entity.onGround() && jumpCooldown <= 0) {
            // Try to climb wall by jumping high
            performHighJump(target);
        }
        // Check for high jump when target is far but reachable
        else if (jumpCooldown <= 0 && this.entity.onGround() && distanceSqr <= highJumpDistanceSqr && distanceSqr > attackDistanceSqr) {
            if (targetIsAbove) {
                // High jump towards elevated target
                performHighJump(target);
            } else {
                // Normal high jump towards target
                performJump(target);
            }
        }
        // Normal jump when close
        else if (jumpCooldown <= 0 && this.entity.onGround() && distanceSqr <= jumpDistanceSqr && distanceSqr > attackDistanceSqr) {
            performJump(target);
        }

        if (lungeCooldown > 0) {
            lungeCooldown--;
        }

        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        // Move towards target (but not while lunging)
        if (distanceSqr > attackDistanceSqr && !isLunging) {
            this.entity.getNavigation().moveTo(target, this.speedModifier);
        } else if (!isLunging) {
            this.entity.getNavigation().stop();
        }

        // Attack when close enough
        if (distanceSqr <= attackDistanceSqr && attackCooldown <= 0) {
            this.entity.setExoMutantAttack(true);
            this.entity.swing(InteractionHand.MAIN_HAND);
            this.entity.doHurtTarget(target);
            attackCooldown = ATTACK_COOLDOWN;
        } else {
            this.entity.setExoMutantAttack(false);
        }
    }

    private void performHighJump(LivingEntity target) {
        Vec3 vec3 = this.entity.getDeltaMovement();
        Vec3 vec31 = new Vec3(target.getX() - this.entity.getX(), 0.0D, target.getZ() - this.entity.getZ());

        if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().scale(0.6D).add(vec3.scale(0.2D));
        }

        // Very high jump for reaching elevated targets or climbing walls
        double heightDiff = target.getY() - this.entity.getY();
        double jumpHeight = Math.min(1.2D + (heightDiff * 0.1D), 1.5D); // Up to 1.5 blocks high jump

        this.entity.setDeltaMovement(vec31.x, jumpHeight, vec31.z);
        this.entity.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN;
        isJumping = true;
    }

    private void performJump(LivingEntity target) {
        Vec3 vec3 = this.entity.getDeltaMovement();
        Vec3 vec31 = new Vec3(target.getX() - this.entity.getX(), 0.0D, target.getZ() - this.entity.getZ());

        if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().scale(0.5D).add(vec3.scale(0.2D));
        }

        // High jump towards target
        this.entity.setDeltaMovement(vec31.x, 0.9D, vec31.z); // 0.9D is high jump
        this.entity.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN;
        isJumping = true;
    }

    /**
     * Performs a diagonal lunge attack when the target is far away.
     * The entity lunges forward and upward at an angle to quickly close the distance.
     */
    private void performDiagonalLunge(LivingEntity target) {
        Vec3 vec3 = this.entity.getDeltaMovement();
        Vec3 toTarget = new Vec3(target.getX() - this.entity.getX(), 0.0D, target.getZ() - this.entity.getZ());
        
        // Calculate horizontal direction to target
        if (toTarget.lengthSqr() > 1.0E-7D) {
            toTarget = toTarget.normalize();
        }
        
        // Calculate height difference
        double heightDiff = target.getY() - this.entity.getY();
        
        // Diagonal lunge: strong forward movement with upward arc
        // The further the target, the more forward momentum
        double distance = Math.sqrt(this.entity.distanceToSqr(target));
        double forwardSpeed = Math.min(1.2D, 0.6D + (distance * 0.05D)); // Faster when further away
        double upwardSpeed = 0.6D + Math.max(0.0D, heightDiff * 0.1D); // Adjust for height difference
        
        Vec3 lungeVector = toTarget.scale(forwardSpeed).add(0.0D, upwardSpeed, 0.0D);
        
        // Add some of the current momentum for smoother movement
        lungeVector = lungeVector.add(vec3.scale(0.2D));
        
        this.entity.setDeltaMovement(lungeVector.x, lungeVector.y, lungeVector.z);
        this.entity.setJumping(true);
        lungeCooldown = LUNGE_COOLDOWN;
        isLunging = true;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        // Attack is handled in tick() method
    }
}

