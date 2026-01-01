package net.jerika.furmutage.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * AI Goal that improves pathfinding and jumping abilities for Changed mod entities.
 * Helps them jump over blocks, cross gaps, and navigate obstacles better.
 */
public class ChangedEntityImprovedPathfindingGoal extends Goal {
    private final PathfinderMob mob;
    private LivingEntity target;
    private static final double JUMP_DISTANCE = 4.0D; // Jump when within 4 blocks of obstacle
    private static final double GAP_JUMP_DISTANCE = 3.0D; // Jump across gaps up to 3 blocks
    private static final double MAX_OBSTACLE_HEIGHT = 2.0D; // Can jump over obstacles up to 2 blocks high
    private int jumpCooldown = 0;
    private static final int JUMP_COOLDOWN = 15; // Cooldown of 0.75 seconds (15 ticks)
    private boolean isJumping = false;

    public ChangedEntityImprovedPathfindingGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        return this.target != null && this.target.isAlive() && this.mob.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.mob.onGround() && !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        jumpCooldown = 0;
        isJumping = false;
    }

    @Override
    public void stop() {
        this.target = null;
        isJumping = false;
    }

    @Override
    public void tick() {
        if (this.target == null || !this.target.isAlive()) {
            return;
        }

        // Reset jumping state when on ground
        if (this.mob.onGround() && isJumping && jumpCooldown <= 5) {
            this.mob.setJumping(false);
            isJumping = false;
        }

        double distanceSqr = this.mob.distanceToSqr(this.target);
        
        // Check if pathfinding is stuck or blocked
        Path currentPath = this.mob.getNavigation().getPath();
        boolean pathBlocked = currentPath == null || currentPath.isDone() || isPathBlocked(currentPath);
        
        // Check for obstacles in the way
        if (jumpCooldown <= 0 && this.mob.onGround() && distanceSqr > 4.0D) {
            // Check for block obstacle first (jump over blocks in the way)
            if (isBlockObstacleAhead()) {
                performObstacleJump();
            }
            // Check for gap ahead
            else if (isGapAhead()) {
                performGapJump();
            }
            // Check if target is 2 blocks above us and we need to jump up
            else if (isTarget2BlocksHigher() && distanceSqr <= JUMP_DISTANCE * JUMP_DISTANCE) {
                performVerticalJump();
            }
        }

        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        // Improve pathfinding by trying to create a better path
        if (pathBlocked && !isJumping) {
            // Try to find an alternative path
            Path newPath = this.mob.getNavigation().createPath(this.target, 0);
            double baseSpeed = this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
            // Use slower speed for better pathfinding and less aggressive movement
            double pathfindingSpeed = baseSpeed * 0.6; // Slower for better pathfinding
            if (newPath != null && newPath.canReach()) {
                this.mob.getNavigation().moveTo(newPath, pathfindingSpeed);
            } else {
                // If no path found, try direct movement at slower speed
                this.mob.getNavigation().moveTo(this.target, pathfindingSpeed);
            }
        }
    }

    private boolean isPathBlocked(Path path) {
        if (path == null || path.getNodeCount() == 0) {
            return true;
        }
        
        // Check if the path has very few nodes (might be blocked)
        if (path.getNodeCount() < 3) {
            return true;
        }
        
        // Check if we're not making progress (entity is stuck)
        // If path exists but has very few nodes and isn't done, it might be blocked
        if (!path.isDone() && path.getNodeCount() < 5) {
            // Check if entity has been trying to follow this path for a while without progress
            // This is a simple heuristic - if path is short and not done, it might be blocked
            return true;
        }
        
        return false;
    }

    private boolean isBlockObstacleAhead() {
        BlockPos mobPos = this.mob.blockPosition();
        Vec3 lookVec = this.mob.getLookAngle();
        BlockPos checkPos = mobPos.offset(
            (int) Math.signum(lookVec.x),
            0,
            (int) Math.signum(lookVec.z)
        );
        
        // Check if there's a solid block in front
        BlockState frontBlock = this.mob.level().getBlockState(checkPos);
        BlockState frontBlockAbove = this.mob.level().getBlockState(checkPos.above());
        
        // Check if there's a block in front but space above to jump
        boolean hasBlockInFront = !frontBlock.isAir() && !frontBlock.getCollisionShape(this.mob.level(), checkPos).isEmpty();
        boolean hasSpaceAbove = frontBlockAbove.isAir() || frontBlockAbove.getCollisionShape(this.mob.level(), checkPos.above()).isEmpty();
        
        // Special case: Check for fences (they're 1.5 blocks tall and jumpable)
        boolean isFence = isFenceBlock(frontBlock);
        if (isFence && hasSpaceAbove) {
            return true; // Fences are always jumpable if there's space above
        }
        
        if (hasBlockInFront && hasSpaceAbove) {
            // Check if the block is not too high
            double blockHeight = checkPos.getY() + 1.0;
            double mobY = this.mob.getY();
            double heightDiff = blockHeight - mobY;
            
            return heightDiff <= MAX_OBSTACLE_HEIGHT && heightDiff > 0.5D;
        }
        
        return false;
    }
    
    private boolean isFenceBlock(BlockState state) {
        // Check if block is a fence by checking block type or tags
        Block block = state.getBlock();
        
        // Check if it's a fence block type
        if (block instanceof FenceBlock) {
            return true;
        }
        
        // Check if it's in the fence tag (covers all fence variants)
        if (state.is(BlockTags.FENCES)) {
            return true;
        }
        
        // Check for common fence blocks by name (fallback)
        String blockName = block.getDescriptionId().toLowerCase();
        return blockName.contains("fence") && !blockName.contains("gate");
    }

    private boolean isGapAhead() {
        BlockPos mobPos = this.mob.blockPosition();
        Vec3 lookVec = this.mob.getLookAngle();
        
        // Check 1-3 blocks ahead for gaps
        for (int i = 1; i <= 3; i++) {
            BlockPos checkPos = mobPos.offset(
                (int) (lookVec.x * i),
                -1,
                (int) (lookVec.z * i)
            );
            
            BlockState groundBlock = this.mob.level().getBlockState(checkPos);
            
            // If there's no solid ground, it's a gap
            boolean isGap = groundBlock.isAir() || groundBlock.getCollisionShape(this.mob.level(), checkPos).isEmpty();
            if (isGap) {
                // Check if there's ground after the gap
                BlockPos afterGapPos = mobPos.offset(
                    (int) (lookVec.x * (i + 1)),
                    -1,
                    (int) (lookVec.z * (i + 1))
                );
                BlockState afterGapBlock = this.mob.level().getBlockState(afterGapPos);
                
                // If there's ground after the gap, we can jump across
                boolean hasGroundAfter = !afterGapBlock.isAir() && !afterGapBlock.getCollisionShape(this.mob.level(), afterGapPos).isEmpty();
                if (hasGroundAfter && i <= GAP_JUMP_DISTANCE) {
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean isTarget2BlocksHigher() {
        double heightDiff = this.target.getY() - this.mob.getY();
        return heightDiff >= 2.0D; // Target must be at least 2 blocks higher
    }

    private void performObstacleJump() {
        Vec3 lookVec = this.mob.getLookAngle();
        
        // Check if we're jumping over a fence (needs slightly different jump)
        BlockPos mobPos = this.mob.blockPosition();
        BlockPos checkPos = mobPos.offset(
            (int) Math.signum(lookVec.x),
            0,
            (int) Math.signum(lookVec.z)
        );
        BlockState frontBlock = this.mob.level().getBlockState(checkPos);
        boolean isFence = isFenceBlock(frontBlock);
        
        double jumpHeight = isFence ? 0.5D : 0.6D; // Reduced jump height
        double forwardMomentum = isFence ? 0.4D : 0.35D; // Reduced forward momentum
        
        Vec3 jumpVec = new Vec3(
            lookVec.x * forwardMomentum,
            jumpHeight,
            lookVec.z * forwardMomentum
        );
        
        this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(jumpVec));
        this.mob.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN;
        isJumping = true;
    }

    private void performGapJump() {
        Vec3 lookVec = this.mob.getLookAngle();
        Vec3 jumpVec = new Vec3(
            lookVec.x * 0.4D, // Reduced forward momentum for gap crossing
            0.4D, // Reduced jump height for gaps
            lookVec.z * 0.4D
        );
        
        this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(jumpVec));
        this.mob.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN;
        isJumping = true;
    }

    private void performVerticalJump() {
        Vec3 lookVec = this.mob.getLookAngle();
        double heightDiff = this.target.getY() - this.mob.getY();
        double jumpHeight = Math.min(0.5D + (heightDiff * 0.15D), 0.8D); // Reduced jump height
        
        Vec3 jumpVec = new Vec3(
            lookVec.x * 0.3D, // Reduced forward momentum
            jumpHeight,
            lookVec.z * 0.3D
        );
        
        this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(jumpVec));
        this.mob.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN;
        isJumping = true;
    }
}

