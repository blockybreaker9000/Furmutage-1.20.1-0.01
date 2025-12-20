package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.LatexBacteriaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class BacteriaJumpAttackGoal extends MeleeAttackGoal {
    private final LatexBacteriaEntity entity;
    private static final double JUMP_DISTANCE = 3.0D; // Jump when within 3 blocks
    private static final double LUNGE_DISTANCE = 4.0D; // Lunge when within 4 blocks
    private static final double ATTACK_DISTANCE = 1.5D; // Attack when within 1.5 blocks
    private int jumpCooldown = 0;
    private static final int JUMP_COOLDOWN = 30; // Cooldown of 1.5 seconds (30 ticks)
    private int lungeCooldown = 0;
    private static final int LUNGE_COOLDOWN = 40; // Cooldown of 2 seconds (40 ticks)
    private boolean isJumping = false;
    private boolean isLunging = false;
    private int attackCooldown = 0;
    private static final int ATTACK_COOLDOWN = 20; // Attack cooldown of 1 second (20 ticks)

    public BacteriaJumpAttackGoal(LatexBacteriaEntity entity, double speedModifier, boolean followTargetEvenIfNotSeen) {
        super(entity, speedModifier, followTargetEvenIfNotSeen);
        this.entity = entity;
    }

    @Override
    public void start() {
        super.start();
        jumpCooldown = 0;
        lungeCooldown = 0;
        isJumping = false;
        isLunging = false;
    }

    @Override
    public void stop() {
        super.stop();
        isJumping = false;
        isLunging = false;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        // Reset jumping state when on ground and not actively jumping
        if (this.entity.onGround() && (isJumping || isLunging) && jumpCooldown <= 10 && lungeCooldown <= 10) {
            this.entity.setJumping(false);
            isJumping = false;
            isLunging = false;
        }

        double distanceSqr = this.entity.distanceToSqr(target);
        double jumpDistanceSqr = JUMP_DISTANCE * JUMP_DISTANCE;
        double lungeDistanceSqr = LUNGE_DISTANCE * LUNGE_DISTANCE;
        double attackDistanceSqr = ATTACK_DISTANCE * ATTACK_DISTANCE;

        // Look at target
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Check for lunge attack first (most aggressive)
        if (lungeCooldown <= 0 && this.entity.onGround() && distanceSqr <= lungeDistanceSqr && distanceSqr > attackDistanceSqr) {
            performLungeAttack(target);
        }
        // Check if there's a block blocking the path and jump over it
        else if (jumpCooldown <= 0 && this.entity.onGround() && distanceSqr > attackDistanceSqr) {
            if (isBlockInWay()) {
                performBlockJump(target);
            } else if (distanceSqr <= jumpDistanceSqr && distanceSqr > attackDistanceSqr) {
                // Jump towards target when close
                performJump(target);
            }
        }

        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        if (lungeCooldown > 0) {
            lungeCooldown--;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        // Move towards target
        if (distanceSqr > attackDistanceSqr && !isJumping && !isLunging) {
            Path path = this.entity.getNavigation().createPath(target, 0);
            if (path != null) {
                this.entity.getNavigation().moveTo(path, 1.0D);
            } else {
                this.entity.getNavigation().moveTo(target, 1.0D);
            }
        } else if (distanceSqr <= attackDistanceSqr) {
            // Close enough to attack
            this.entity.getNavigation().stop();
            this.entity.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            
            // Perform attack
            if (attackCooldown <= 0) {
                this.entity.swing(InteractionHand.MAIN_HAND);
                this.entity.doHurtTarget(target);
                this.entity.setJumping(false);
                isLunging = false;
                attackCooldown = ATTACK_COOLDOWN;
            }
        }
    }

    private void performLungeAttack(LivingEntity target) {
        Vec3 vec3 = this.entity.getDeltaMovement();
        Vec3 vec31 = new Vec3(target.getX() - this.entity.getX(), 0.0D, target.getZ() - this.entity.getZ());
        
        if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().scale(0.8D); // Fast horizontal lunge
        }
        
        // Lunge forward with a slight upward arc
        this.entity.setDeltaMovement(vec31.x, 0.4D, vec31.z); // Lower jump but faster forward
        this.entity.setJumping(true);
        lungeCooldown = LUNGE_COOLDOWN;
        isLunging = true;
        
        // Try to attack during lunge if close enough
        double distanceSqr = this.entity.distanceToSqr(target);
        if (distanceSqr <= ATTACK_DISTANCE * ATTACK_DISTANCE && attackCooldown <= 0) {
            this.entity.swing(InteractionHand.MAIN_HAND);
            this.entity.doHurtTarget(target);
            attackCooldown = ATTACK_COOLDOWN;
        }
    }

    private boolean isBlockInWay() {
        // Check if there's a solid block in front of the entity that would block movement
        BlockPos entityPos = this.entity.blockPosition();
        Vec3 lookVec = this.entity.getLookAngle();
        
        // Get the direction the entity is moving towards the target
        LivingEntity target = this.entity.getTarget();
        if (target == null) {
            return false;
        }
        
        double dx = target.getX() - this.entity.getX();
        double dz = target.getZ() - this.entity.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        
        if (distance < 0.1) {
            return false;
        }
        
        // Normalize direction
        dx /= distance;
        dz /= distance;
        
        // Check blocks in the direction of movement
        BlockPos frontPos = entityPos.offset(
            (int) Math.signum(dx),
            0,
            (int) Math.signum(dz)
        );
        
        // Check if the block in front is solid and would block movement
        BlockState frontBlock = this.entity.level().getBlockState(frontPos);
        BlockState frontBlockAbove = this.entity.level().getBlockState(frontPos.above());
        BlockState frontBlockBelow = this.entity.level().getBlockState(frontPos.below());
        
        // Check if there's a solid block in front at ground level or one block up
        boolean hasSolidInFront = frontBlock.isSolid() || frontBlockAbove.isSolid();
        
        // Check if the space above the front block is clear (so we can jump over)
        BlockState frontBlockTwoAbove = this.entity.level().getBlockState(frontPos.above(2));
        boolean canJumpOver = !frontBlockTwoAbove.isSolid();
        
        // Also check if the entity's current position has clear space above
        BlockState headBlock = this.entity.level().getBlockState(entityPos.above());
        BlockState headBlockTwo = this.entity.level().getBlockState(entityPos.above(2));
        boolean hasClearSpaceAbove = !headBlock.isSolid() && !headBlockTwo.isSolid();
        
        // Jump if there's a solid block in front but we can jump over it
        if (hasSolidInFront && canJumpOver && hasClearSpaceAbove) {
            return true;
        }
        
        return false;
    }

    private void performBlockJump(LivingEntity target) {
        Vec3 vec3 = this.entity.getDeltaMovement();
        Vec3 vec31 = new Vec3(target.getX() - this.entity.getX(), 0.0D, target.getZ() - this.entity.getZ());
        
        if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().scale(0.5D).add(vec3.scale(0.2D));
        }
        
        // Jump over block - higher jump for obstacles
        this.entity.setDeltaMovement(vec31.x, 0.7D, vec31.z); // 0.7D is higher jump for blocks
        this.entity.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN / 2; // Shorter cooldown for obstacle jumps
        isJumping = true;
    }

    private void performJump(LivingEntity target) {
        Vec3 vec3 = this.entity.getDeltaMovement();
        Vec3 vec31 = new Vec3(target.getX() - this.entity.getX(), 0.0D, target.getZ() - this.entity.getZ());
        
        if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().scale(0.4D).add(vec3.scale(0.2D));
        }
        
        // Jump towards target
        this.entity.setDeltaMovement(vec31.x, 0.6D, vec31.z); // 0.6D is jump height
        this.entity.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN;
        isJumping = true;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        // Attack is handled in tick() method
    }
}

