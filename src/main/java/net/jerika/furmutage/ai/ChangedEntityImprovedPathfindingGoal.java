package net.jerika.furmutage.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.VineBlock;
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
    private static final double MAX_OBSTACLE_HEIGHT = 3.0D; // Can jump over obstacles up to 3 blocks high
    private static final double MIN_TARGET_HEIGHT_DIFF = 2.0D; // Minimum height difference to trigger jump (2 blocks)
    private static final double MAX_JUMP_HEIGHT = 5.0D; // Maximum jump height (5 blocks)
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
        
        // Check for climbable blocks (vines/ladders) first - highest priority
        if (isTargetAbove() && distanceSqr > 4.0D) {
            BlockPos climbablePos = findClimbableBlock();
            if (climbablePos != null) {
                // Move towards and climb the vine/ladder
                performClimb(climbablePos);
                return; // Don't do other pathfinding while climbing
            }
        }
        
        // Check for obstacles in the way
        if (jumpCooldown <= 0 && this.mob.onGround() && distanceSqr > 6.0D) {
            // Check if target is higher than 2 blocks - prioritize jumping towards elevated targets
            double heightDiff = this.target.getY() - this.mob.getY();
            if (heightDiff > MIN_TARGET_HEIGHT_DIFF && heightDiff <= MAX_JUMP_HEIGHT) {
                // Target is between 2 and 5 blocks higher - jump towards it
                performJumpTowardsTarget();
            }
            // Check for block obstacle first (jump over blocks in the way)
            else if (isBlockObstacleAhead()) {
                performObstacleJump();
            }
            // Check for gap ahead
            else if (isGapAhead()) {
                performGapJump();
            }
            // Check if target is 2 blocks above us and we need to jump up (legacy check)
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
        
        // Check for gaps up to 3 blocks ahead
        // We need to find where the gap starts and how wide it is
        int gapStart = -1;
        int gapWidth = 0;
        
        // First, find where the gap starts (where ground disappears)
        for (int i = 1; i <= (int)GAP_JUMP_DISTANCE + 1; i++) {
            BlockPos checkPos = mobPos.offset(
                (int) Math.round(lookVec.x * i),
                -1,
                (int) Math.round(lookVec.z * i)
            );
            
            BlockState groundBlock = this.mob.level().getBlockState(checkPos);
            boolean hasGround = !groundBlock.isAir() && !groundBlock.getCollisionShape(this.mob.level(), checkPos).isEmpty();
            
            if (!hasGround && gapStart == -1) {
                // Gap starts here
                gapStart = i;
            } else if (!hasGround && gapStart != -1) {
                // Continue counting gap width
                gapWidth++;
            } else if (hasGround && gapStart != -1) {
                // Gap ends here, check if we can jump across
                if (gapWidth <= GAP_JUMP_DISTANCE) {
                    // Check if there's solid ground after the gap
                    BlockPos afterGapPos = mobPos.offset(
                        (int) Math.round(lookVec.x * (gapStart + gapWidth + 1)),
                        -1,
                        (int) Math.round(lookVec.z * (gapStart + gapWidth + 1))
                    );
                    BlockState afterGapBlock = this.mob.level().getBlockState(afterGapPos);
                    boolean hasGroundAfter = !afterGapBlock.isAir() && !afterGapBlock.getCollisionShape(this.mob.level(), afterGapPos).isEmpty();
                    
                    if (hasGroundAfter) {
                        return true;
                    }
                }
                // Reset for next potential gap
                gapStart = -1;
                gapWidth = 0;
            }
        }
        
        // Also check for immediate gap (ground disappears right in front)
        BlockPos immediatePos = mobPos.offset(
            (int) Math.round(lookVec.x),
            -1,
            (int) Math.round(lookVec.z)
        );
        BlockState immediateGround = this.mob.level().getBlockState(immediatePos);
        boolean hasImmediateGround = !immediateGround.isAir() && !immediateGround.getCollisionShape(this.mob.level(), immediatePos).isEmpty();
        
        if (!hasImmediateGround) {
            // Check if there's ground 2-3 blocks ahead (gap of 1-2 blocks)
            for (int i = 2; i <= (int)GAP_JUMP_DISTANCE + 1; i++) {
                BlockPos checkPos = mobPos.offset(
                    (int) Math.round(lookVec.x * i),
                    -1,
                    (int) Math.round(lookVec.z * i)
                );
                BlockState groundBlock = this.mob.level().getBlockState(checkPos);
                boolean hasGround = !groundBlock.isAir() && !groundBlock.getCollisionShape(this.mob.level(), checkPos).isEmpty();
                
                if (hasGround) {
                    // Found ground after gap, check gap width
                    int gapSize = i - 1; // Gap is from position 1 to position i-1
                    if (gapSize <= GAP_JUMP_DISTANCE) {
                        return true;
                    }
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
        
        // Calculate gap distance to adjust jump strength
        BlockPos mobPos = this.mob.blockPosition();
        double gapDistance = 0.0D;
        
        // Find the gap distance
        for (int i = 1; i <= (int)GAP_JUMP_DISTANCE + 1; i++) {
            BlockPos checkPos = mobPos.offset(
                (int) Math.round(lookVec.x * i),
                -1,
                (int) Math.round(lookVec.z * i)
            );
            BlockState groundBlock = this.mob.level().getBlockState(checkPos);
            boolean hasGround = !groundBlock.isAir() && !groundBlock.getCollisionShape(this.mob.level(), checkPos).isEmpty();
            
            if (!hasGround) {
                gapDistance = i;
            } else if (hasGround && gapDistance > 0) {
                // Found ground after gap
                break;
            }
        }
        
        // Adjust jump strength based on gap distance
        // For 1 block gap: lighter jump, for 3 block gap: stronger jump
        double forwardMomentum = Math.min(0.5D, 0.35D + (gapDistance * 0.05D));
        double jumpHeight = Math.min(0.5D, 0.35D + (gapDistance * 0.05D));
        
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

    /**
     * Jump towards a target that is elevated (2-5 blocks higher).
     * Calculates the jump vector to reach the target's height.
     */
    private void performJumpTowardsTarget() {
        Vec3 mobPos = this.mob.position();
        Vec3 targetPos = this.target.position();
        
        // Calculate direction to target
        Vec3 toTarget = new Vec3(
            targetPos.x - mobPos.x,
            0.0D, // Horizontal only for direction
            targetPos.z - mobPos.z
        );
        
        double horizontalDistance = toTarget.length();
        double heightDiff = targetPos.y - mobPos.y;
        
        // Clamp height difference to max jump height
        double effectiveHeightDiff = Math.min(heightDiff, MAX_JUMP_HEIGHT);
        
        // Normalize horizontal direction
        if (horizontalDistance > 0.001D) {
            toTarget = toTarget.normalize();
        } else {
            // If directly above, jump straight up
            toTarget = new Vec3(0.0D, 0.0D, 0.0D);
        }
        
        // Calculate jump height based on height difference
        // Use a formula that scales with height: base jump + height scaling
        // For 2 blocks: ~0.6, for 5 blocks: ~1.2
        double jumpHeight = 0.4D + (effectiveHeightDiff * 0.16D);
        jumpHeight = Math.min(jumpHeight, 1.2D); // Cap at reasonable jump height
        
        // Calculate forward momentum based on distance
        // Closer targets get more forward momentum, farther targets get less
        double forwardMomentum = Math.min(0.5D, 0.3D + (horizontalDistance * 0.05D));
        forwardMomentum = Math.max(forwardMomentum, 0.2D); // Minimum forward momentum
        
        Vec3 jumpVec = new Vec3(
            toTarget.x * forwardMomentum,
            jumpHeight,
            toTarget.z * forwardMomentum
        );
        
        this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(jumpVec));
        this.mob.setJumping(true);
        jumpCooldown = JUMP_COOLDOWN;
        isJumping = true;
    }

    /**
     * Check if the target is above the mob
     */
    private boolean isTargetAbove() {
        return this.target.getY() > this.mob.getY();
    }

    /**
     * Find a climbable block (vine or ladder) near the mob that leads towards the target
     */
    private BlockPos findClimbableBlock() {
        BlockPos mobPos = this.mob.blockPosition();
        Vec3 toTarget = new Vec3(
            this.target.getX() - this.mob.getX(),
            this.target.getY() - this.mob.getY(),
            this.target.getZ() - this.mob.getZ()
        ).normalize();
        
        // Search in a 5x5x8 area around the mob, prioritizing blocks towards the target
        int searchRadius = 5;
        int searchHeight = 8;
        
        BlockPos bestClimbable = null;
        double bestDistance = Double.MAX_VALUE;
        
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int y = 0; y <= searchHeight; y++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos checkPos = mobPos.offset(x, y, z);
                    BlockState state = this.mob.level().getBlockState(checkPos);
                    
                    // Check if this is a climbable block
                    if (isClimbableBlock(state)) {
                        // Check if there's a path of climbable blocks going up
                        if (hasClimbablePathUp(checkPos)) {
                            double distance = mobPos.distSqr(checkPos);
                            // Prefer blocks closer to the target direction
                            Vec3 toBlock = new Vec3(
                                checkPos.getX() - mobPos.getX(),
                                checkPos.getY() - mobPos.getY(),
                                checkPos.getZ() - mobPos.getZ()
                            ).normalize();
                            
                            // Weight by how well it aligns with target direction
                            double alignment = toBlock.dot(toTarget);
                            double weightedDistance = distance / (1.0 + alignment * 2.0);
                            
                            if (weightedDistance < bestDistance) {
                                bestDistance = weightedDistance;
                                bestClimbable = checkPos;
                            }
                        }
                    }
                }
            }
        }
        
        return bestClimbable;
    }

    /**
     * Check if a block is climbable (vine or ladder)
     */
    private boolean isClimbableBlock(BlockState state) {
        Block block = state.getBlock();
        
        // Check for vines
        if (block instanceof VineBlock) {
            return true;
        }
        
        // Check for ladders
        if (block instanceof LadderBlock) {
            return true;
        }
        
        // Check for climbable tag (covers vines, ladders, etc.)
        if (state.is(BlockTags.CLIMBABLE)) {
            return true;
        }
        
        return false;
    }

    /**
     * Check if there's a continuous path of climbable blocks going upward from this position
     */
    private boolean hasClimbablePathUp(BlockPos startPos) {
        // Check at least 3 blocks up to ensure there's a path
        for (int y = 0; y <= 3; y++) {
            BlockPos checkPos = startPos.above(y);
            BlockState state = this.mob.level().getBlockState(checkPos);
            
            if (!isClimbableBlock(state)) {
                // If we hit a non-climbable block, check if it's air (we can still climb past it)
                if (state.isAir()) {
                    continue; // Air is fine, we can climb past it
                } else {
                    return false; // Solid block blocks climbing
                }
            }
        }
        
        return true;
    }

    /**
     * Make the mob climb towards a climbable block
     */
    private void performClimb(BlockPos climbablePos) {
        Vec3 mobPos = this.mob.position();
        Vec3 targetPos = new Vec3(
            climbablePos.getX() + 0.5,
            climbablePos.getY(),
            climbablePos.getZ() + 0.5
        );
        
        Vec3 direction = targetPos.subtract(mobPos).normalize();
        
        // Move towards the climbable block
        double baseSpeed = this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
        double climbSpeed = baseSpeed * 0.8; // Slightly slower when climbing
        
        // If we're close to the climbable block, move upward
        double distanceToClimbable = mobPos.distanceTo(targetPos);
        if (distanceToClimbable < 1.5D) {
            // We're at the climbable block, move upward
            // For ladders, Minecraft's built-in climbing will handle the movement
            // We just need to ensure the entity is positioned correctly
            Vec3 upwardMovement = new Vec3(
                direction.x * climbSpeed * 0.2, // Reduced horizontal movement when climbing
                0.2D, // Upward movement for climbing (Minecraft will handle the rest)
                direction.z * climbSpeed * 0.2
            );
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(upwardMovement));
            
            // Also try to move directly towards the ladder position
            this.mob.getNavigation().moveTo(climbablePos.getX() + 0.5, climbablePos.getY() + 1, climbablePos.getZ() + 0.5, climbSpeed);
        } else {
            // Move towards the climbable block
            this.mob.getNavigation().moveTo(climbablePos.getX() + 0.5, climbablePos.getY(), climbablePos.getZ() + 0.5, climbSpeed);
        }
        
        // Look at the climbable block
        this.mob.getLookControl().setLookAt(targetPos.x, targetPos.y + 1, targetPos.z, 30.0F, 30.0F);
    }
}

