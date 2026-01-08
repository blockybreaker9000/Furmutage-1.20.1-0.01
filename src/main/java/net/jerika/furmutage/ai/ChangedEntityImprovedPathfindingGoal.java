package net.jerika.furmutage.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
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
    private static final double JUMP_DISTANCE = 2.0D; // Jump when within 2 blocks of obstacle
    private static final double GAP_JUMP_DISTANCE = 4.0D; // Jump across gaps up to 3 blocks
    private static final double MIN_WALL_HEIGHT = 2.0D; // Minimum wall height to jump (2 blocks)
    private static final double MAX_OBSTACLE_HEIGHT = 6.0D; // Can jump over obstacles up to 6 blocks high
    private static final double MIN_TARGET_HEIGHT_DIFF = 2.0D; // Minimum height difference to trigger jump (2 blocks)
    private static final double MAX_JUMP_HEIGHT = 6.0D; // Maximum jump height (6 blocks)
    private int jumpCooldown = 0;
    private static final int JUMP_COOLDOWN = 15; // Cooldown of 0.75 seconds (15 ticks)
    private boolean isJumping = false;
    private boolean isCrawling = false;
    private int crawlCheckCooldown = 0;
    private static final int CRAWL_CHECK_COOLDOWN = 5; // Check every 5 ticks
    private boolean needsCrawlJump = false; // Flag to track if we need to crawl then jump through a gap
    private int crawlPrepTime = 0; // Time spent preparing to jump (crawling first)
    private static final int CRAWL_PREP_TIME = 5; // Crawl for 5 ticks before jumping

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
        isCrawling = false;
        crawlCheckCooldown = 0;
        needsCrawlJump = false;
        crawlPrepTime = 0;
    }

    @Override
    public void stop() {
        this.target = null;
        isJumping = false;
        needsCrawlJump = false;
        crawlPrepTime = 0;
        // Reset crawling pose when stopping
        if (isCrawling) {
            this.mob.setPose(Pose.STANDING);
            isCrawling = false;
        }
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
            // Exit crawl mode after landing if we were crawling for a jump
            if (isCrawling && !isInCrawlSpace() && !needsCrawlJump) {
                this.mob.setPose(Pose.STANDING);
                isCrawling = false;
            }
        }

        double distanceSqr = this.mob.distanceToSqr(this.target);
        
        // Check if pathfinding is stuck or blocked
        Path currentPath = this.mob.getNavigation().getPath();
        boolean pathBlocked = currentPath == null || currentPath.isDone() || isPathBlocked(currentPath);
        
        // Check for climbable blocks (ladders only - no vines) first - highest priority
        if (isTargetAbove() && distanceSqr > 4.0D) {
            BlockPos climbablePos = findClimbableBlock();
            if (climbablePos != null) {
                // Move towards and climb the ladder
                performClimb(climbablePos);
                // Reset crawling if we're climbing
                if (isCrawling) {
                    this.mob.setPose(Pose.STANDING);
                    isCrawling = false;
                }
                return; // Don't do other pathfinding while climbing
            }
        }
        
        // Check for 1-block gaps that require crawling - check before jumping
        if (crawlCheckCooldown <= 0 && this.mob.onGround() && !isJumping) {
            if (needsCrawling()) {
                performCrawling();
            } else if (isCrawling && !isInCrawlSpace() && !needsCrawlJump) {
                // Stop crawling if we're no longer in a crawl space and not preparing to jump
                this.mob.setPose(Pose.STANDING);
                isCrawling = false;
            }
            crawlCheckCooldown = CRAWL_CHECK_COOLDOWN;
        } else if (crawlCheckCooldown > 0) {
            crawlCheckCooldown--;
        }
        
        // Handle crawl-then-jump sequence for 1-block gaps
        if (needsCrawlJump && this.mob.onGround()) {
            if (!isCrawling) {
                // Start crawling
                this.mob.setPose(Pose.SWIMMING);
                isCrawling = true;
                crawlPrepTime = 0;
            } else {
                crawlPrepTime++;
                // After crawling for a bit, perform the jump
                if (crawlPrepTime >= CRAWL_PREP_TIME && jumpCooldown <= 0) {
                    performOneBlockGapJump();
                    needsCrawlJump = false;
                    crawlPrepTime = 0;
                }
            }
            // Continue moving towards the gap while crawling
            double baseSpeed = this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
            double crawlSpeed = baseSpeed * 0.7;
            Path path = this.mob.getNavigation().createPath(this.target, 0);
            if (path != null && path.canReach()) {
                this.mob.getNavigation().moveTo(path, crawlSpeed);
            } else {
                this.mob.getNavigation().moveTo(this.target, crawlSpeed);
            }
        }
        
        // Check for obstacles in the way
        if (jumpCooldown <= 0 && this.mob.onGround() && distanceSqr > 6.0D && !needsCrawlJump) {
            // Check if target is higher than 2 blocks - prioritize jumping towards elevated targets
            double heightDiff = this.target.getY() - this.mob.getY();
            if (heightDiff > MIN_TARGET_HEIGHT_DIFF && heightDiff <= MAX_JUMP_HEIGHT) {
                // Target is between 2 and 5 blocks higher - jump towards it
                performJumpTowardsTarget();
            }
            // Check for 1-block gap one block above ground (crawl then jump through opening)
            else if (isOneBlockGapAboveGround()) {
                needsCrawlJump = true;
                crawlPrepTime = 0;
            }
            // Check for block obstacle first (jump over blocks in the way)
            else if (isBlockObstacleAhead()) {
                performObstacleJump();
            }
            // Check for gap ahead
            else if (isGapAhead()) {
                performGapJump();
            }
            // Check if target is 2 blocks above us, and we need to jump up (legacy check)
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
        
        // Special case: Check for fences (they're 1.5 blocks tall and jumpable)
        boolean isFence = isFenceBlock(frontBlock);
        if (isFence) {
            BlockState frontBlockAbove = this.mob.level().getBlockState(checkPos.above());
            boolean hasSpaceAbove = frontBlockAbove.isAir() || frontBlockAbove.getCollisionShape(this.mob.level(), checkPos.above()).isEmpty();
            if (hasSpaceAbove) {
                return true; // Fences are always jumpable if there's space above
            }
        }
        
        // Check for walls 2-5 blocks high
        // Count how many solid blocks are stacked vertically
        int wallHeight = 0;
        BlockPos currentCheck = checkPos;
        double mobY = this.mob.getY();
        
        // Check blocks from ground level up to 6 blocks (to detect walls up to 5 blocks high)
        for (int y = 0; y <= 6; y++) {
            BlockPos checkBlockPos = currentCheck.atY((int)Math.floor(mobY) + y);
            BlockState blockState = this.mob.level().getBlockState(checkBlockPos);
            
            // Check if this block is solid (has collision)
            boolean isSolid = !blockState.isAir() && !blockState.getCollisionShape(this.mob.level(), checkBlockPos).isEmpty();
            
            if (isSolid) {
                wallHeight++;
            } else {
                // We've hit air, check if we have a valid wall height
                if (wallHeight >= MIN_WALL_HEIGHT && wallHeight <= MAX_OBSTACLE_HEIGHT) {
                    // Check if there's clear space above the wall for the entity to pass
                    // Need to check if the space above the wall is clear for at least 2 blocks (entity height)
                    boolean hasClearSpaceAbove = true;
                    for (int clearCheck = 1; clearCheck <= 3; clearCheck++) {
                        BlockPos spaceCheckPos = checkBlockPos.above(clearCheck - 1);
                        BlockState spaceBlock = this.mob.level().getBlockState(spaceCheckPos);
                        if (!spaceBlock.isAir() && !spaceBlock.getCollisionShape(this.mob.level(), spaceCheckPos).isEmpty()) {
                            hasClearSpaceAbove = false;
                            break;
                        }
                    }
                    
                    // Also check if entity's current position has clear space above
                    BlockPos entityHeadPos = mobPos.above(1);
                    BlockState entityHeadBlock = this.mob.level().getBlockState(entityHeadPos);
                    boolean entityHasClearSpace = entityHeadBlock.isAir() || entityHeadBlock.getCollisionShape(this.mob.level(), entityHeadPos).isEmpty();
                    
                    if (hasClearSpaceAbove && entityHasClearSpace) {
                        return true; // Found a valid wall 2-5 blocks high that we can jump over
                    }
                }
                // Reset wall height if we hit air before reaching minimum height
                wallHeight = 0;
            }
        }
        
        // Also check if there's a solid block starting from the ground at checkPos
        BlockPos groundCheck = checkPos.below();
        BlockState groundBlock = this.mob.level().getBlockState(groundCheck);
        boolean hasGround = !groundBlock.isAir() && !groundBlock.getCollisionShape(this.mob.level(), groundCheck).isEmpty();
        
        if (hasGround) {
            // Check wall from ground level
            wallHeight = 0;
            for (int y = 0; y <= 6; y++) {
                BlockPos checkBlockPos = groundCheck.above(y + 1);
                BlockState blockState = this.mob.level().getBlockState(checkBlockPos);
                boolean isSolid = !blockState.isAir() && !blockState.getCollisionShape(this.mob.level(), checkBlockPos).isEmpty();
                
                if (isSolid) {
                    wallHeight++;
                } else {
                    if (wallHeight >= MIN_WALL_HEIGHT && wallHeight <= MAX_OBSTACLE_HEIGHT) {
                        // Check clear space above wall
                        boolean hasClearSpaceAbove = true;
                        for (int clearCheck = 1; clearCheck <= 3; clearCheck++) {
                            BlockPos spaceCheckPos = checkBlockPos.above(clearCheck - 1);
                            BlockState spaceBlock = this.mob.level().getBlockState(spaceCheckPos);
                            if (!spaceBlock.isAir() && !spaceBlock.getCollisionShape(this.mob.level(), spaceCheckPos).isEmpty()) {
                                hasClearSpaceAbove = false;
                                break;
                            }
                        }
                        
                        BlockPos entityHeadPos = mobPos.above(1);
                        BlockState entityHeadBlock = this.mob.level().getBlockState(entityHeadPos);
                        boolean entityHasClearSpace = entityHeadBlock.isAir() || entityHeadBlock.getCollisionShape(this.mob.level(), entityHeadPos).isEmpty();
                        
                        if (hasClearSpaceAbove && entityHasClearSpace) {
                            return true;
                        }
                    }
                    wallHeight = 0;
                }
            }
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

    /**
     * Check if there's a 1-block gap one block above the ground that we can jump through
     */
    private boolean isOneBlockGapAboveGround() {
        BlockPos mobPos = this.mob.blockPosition();
        Vec3 lookVec = this.mob.getLookAngle();
        
        // Check the block directly ahead at ground level
        BlockPos frontPos = mobPos.offset(
            (int) Math.signum(lookVec.x),
            0,
            (int) Math.signum(lookVec.z)
        );
        
        // Check if there's a solid block at ground level in front
        BlockState groundBlock = this.mob.level().getBlockState(frontPos);
        boolean hasSolidGround = !groundBlock.isAir() && !groundBlock.getCollisionShape(this.mob.level(), frontPos).isEmpty();
        
        // Check if there's air at 1 block above ground (the gap)
        BlockPos gapPos = frontPos.above(1);
        BlockState gapBlock = this.mob.level().getBlockState(gapPos);
        boolean hasGap = gapBlock.isAir() || gapBlock.getCollisionShape(this.mob.level(), gapPos).isEmpty();
        
        // Check if there's a solid block at 2 blocks above ground (blocking above the gap)
        BlockPos aboveGapPos = frontPos.above(2);
        BlockState aboveGapBlock = this.mob.level().getBlockState(aboveGapPos);
        boolean hasBlockAbove = !aboveGapBlock.isAir() && !aboveGapBlock.getCollisionShape(this.mob.level(), aboveGapPos).isEmpty();
        
        // Also check that the space above us (at entity head level) is clear
        BlockPos mobHeadPos = mobPos.above(1);
        BlockState mobHeadBlock = this.mob.level().getBlockState(mobHeadPos);
        boolean mobHasClearSpace = mobHeadBlock.isAir() || mobHeadBlock.getCollisionShape(this.mob.level(), mobHeadPos).isEmpty();
        
        // Also check if there's space on the other side of the gap to land
        BlockPos beyondGapPos = mobPos.offset(
            (int) Math.signum(lookVec.x) * 2,
            1, // One block above ground
            (int) Math.signum(lookVec.z) * 2
        );
        BlockState beyondGapBlock = this.mob.level().getBlockState(beyondGapPos);
        boolean hasSpaceBeyond = beyondGapBlock.isAir() || beyondGapBlock.getCollisionShape(this.mob.level(), beyondGapPos).isEmpty();
        
        // Check if there's ground below the gap on the other side
        BlockPos groundBeyondPos = beyondGapPos.below();
        BlockState groundBeyondBlock = this.mob.level().getBlockState(groundBeyondPos);
        boolean hasGroundBeyond = !groundBeyondBlock.isAir() && !groundBeyondBlock.getCollisionShape(this.mob.level(), groundBeyondPos).isEmpty();
        
        // A valid 1-block gap one block above ground:
        // 1. Has solid block at ground level in front
        // 2. Has air/gap at 1 block above ground
        // 3. Has solid block at 2 blocks above ground (blocking)
        // 4. Entity has clear space to jump
        // 5. There's space on the other side and ground to land on
        return hasSolidGround && hasGap && hasBlockAbove && mobHasClearSpace && hasSpaceBeyond && hasGroundBeyond;
    }

    /**
     * Jump through a 1-block gap one block above the ground
     * Entity should already be in crawl mode when this is called
     */
    private void performOneBlockGapJump() {
        Vec3 lookVec = this.mob.getLookAngle();
        
        // Ensure we're still in crawl mode for the jump
        if (!isCrawling) {
            this.mob.setPose(Pose.SWIMMING);
            isCrawling = true;
        }
        
        // Jump with moderate height to go through the 1-block gap
        // Need enough height to clear the gap but not too much
        double jumpHeight = 0.55D; // Just enough to get through 1-block gap
        double forwardMomentum = 0.45D; // Moderate forward movement
        
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

    private void performObstacleJump() {
        Vec3 lookVec = this.mob.getLookAngle();
        
        // Calculate wall height to adjust jump strength
        BlockPos mobPos = this.mob.blockPosition();
        BlockPos checkPos = mobPos.offset(
            (int) Math.signum(lookVec.x),
            0,
            (int) Math.signum(lookVec.z)
        );
        BlockState frontBlock = this.mob.level().getBlockState(checkPos);
        boolean isFence = isFenceBlock(frontBlock);
        
        // Measure wall height
        double wallHeight = 1.0D; // Default to 1 block
        if (!isFence) {
            BlockPos currentCheck = checkPos;
            double mobY = this.mob.getY();
            int blocksHigh = 0;
            
            for (int y = 0; y <= 6; y++) {
                BlockPos checkBlockPos = currentCheck.atY((int)Math.floor(mobY) + y);
                BlockState blockState = this.mob.level().getBlockState(checkBlockPos);
                boolean isSolid = !blockState.isAir() && !blockState.getCollisionShape(this.mob.level(), checkBlockPos).isEmpty();
                
                if (isSolid) {
                    blocksHigh++;
                } else if (blocksHigh > 0) {
                    wallHeight = blocksHigh;
                    break;
                }
            }
            
            // Also check from ground level
            BlockPos groundCheck = checkPos.below();
            BlockState groundBlock = this.mob.level().getBlockState(groundCheck);
            boolean hasGround = !groundBlock.isAir() && !groundBlock.getCollisionShape(this.mob.level(), groundCheck).isEmpty();
            
            if (hasGround) {
                blocksHigh = 0;
                for (int y = 0; y <= 6; y++) {
                    BlockPos checkBlockPos = groundCheck.above(y + 1);
                    BlockState blockState = this.mob.level().getBlockState(checkBlockPos);
                    boolean isSolid = !blockState.isAir() && !blockState.getCollisionShape(this.mob.level(), checkBlockPos).isEmpty();
                    
                    if (isSolid) {
                        blocksHigh++;
                    } else if (blocksHigh > 0) {
                        if (blocksHigh > wallHeight) {
                            wallHeight = blocksHigh;
                        }
                        break;
                    }
                }
            }
            
            // Clamp wall height to valid range
            wallHeight = Math.max(MIN_WALL_HEIGHT, Math.min(wallHeight, MAX_OBSTACLE_HEIGHT));
        }
        
        // Calculate jump parameters based on wall height
        // For 2 blocks: base jump, for 5 blocks: maximum jump
        // Jump height scales with higher base and scaling for better wall clearance
        // Increased jump heights: 2 blocks = 0.7, 3 blocks = 1.0, 4 blocks = 1.3, 5 blocks = 1.6
        double jumpHeight = isFence ? 0.6D : 0.7D + (wallHeight - MIN_WALL_HEIGHT) * 0.3D;
        jumpHeight = Math.min(jumpHeight, 1.6D); // Increased cap for 5-block walls
        
        // Forward momentum also scales with wall height (higher walls need more momentum)
        double forwardMomentum = isFence ? 0.5D : 0.45D + (wallHeight - MIN_WALL_HEIGHT) * 0.04D;
        forwardMomentum = Math.min(forwardMomentum, 0.6D); // Slightly increased forward momentum
        
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
     * Find a climbable block (ladder only - no vines) near the mob that leads towards the target
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
                    
                    // Check if this is a climbable block (ladders only, no vines)
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
     * Check if a block is climbable (ladders only - no vines)
     */
    private boolean isClimbableBlock(BlockState state) {
        Block block = state.getBlock();
        
        // Check for ladders only (no vines)
        if (block instanceof LadderBlock) {
            return true;
        }
        
        // Check for climbable tag but exclude vines
        if (state.is(BlockTags.CLIMBABLE)) {
            // Only allow ladders, not vines
            if (!(block instanceof VineBlock)) {
                return true;
            }
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

    /**
     * Check if the entity needs to crawl through a 1-block gap to reach the target
     */
    private boolean needsCrawling() {
        BlockPos mobPos = this.mob.blockPosition();
        Vec3 lookVec = this.mob.getLookAngle();
        
        // Check the space directly ahead of the entity (where it's trying to move)
        BlockPos aheadPos = mobPos.offset(
            (int) Math.signum(lookVec.x),
            0,
            (int) Math.signum(lookVec.z)
        );
        
        // Check if there's a solid block at head height (1 block above entity's feet)
        // This means only 1 block of vertical space available
        BlockPos headPos = aheadPos.above(1);
        BlockState headBlock = this.mob.level().getBlockState(headPos);
        boolean hasBlockAbove = !headBlock.isAir() && !headBlock.getCollisionShape(this.mob.level(), headPos).isEmpty();
        
        // Also check the current position to see if we're already in a crawl space
        BlockPos currentHeadPos = mobPos.above(1);
        BlockState currentHeadBlock = this.mob.level().getBlockState(currentHeadPos);
        boolean currentlyHasBlockAbove = !currentHeadBlock.isAir() && !currentHeadBlock.getCollisionShape(this.mob.level(), currentHeadPos).isEmpty();
        
        // Check if the space ahead is passable (ground exists) but has a block above (1-block gap)
        BlockPos groundPos = aheadPos.below();
        BlockState groundBlock = this.mob.level().getBlockState(groundPos);
        boolean hasGround = !groundBlock.isAir() && !groundBlock.getCollisionShape(this.mob.level(), groundPos).isEmpty();
        
        // Check if the space at entity level is passable
        BlockState spaceBlock = this.mob.level().getBlockState(aheadPos);
        boolean spaceIsPassable = spaceBlock.isAir() || spaceBlock.getCollisionShape(this.mob.level(), aheadPos).isEmpty();
        
        // We need to crawl if:
        // 1. There's a block above at head height (1-block gap) OR we're already in a crawl space
        // 2. The space ahead is passable (air at entity level)
        // 3. There's ground below
        // 4. The target is in that direction (or we're already crawling)
        if ((hasBlockAbove || currentlyHasBlockAbove) && spaceIsPassable && hasGround) {
            // Make sure the target is in the general direction we're trying to crawl
            Vec3 toTarget = new Vec3(
                this.target.getX() - this.mob.getX(),
                0,
                this.target.getZ() - this.mob.getZ()
            ).normalize();
            
            double alignment = lookVec.dot(toTarget);
            // Allow crawling if we're aligned with target direction or already crawling
            return alignment > 0.3 || isCrawling;
        }
        
        return false;
    }

    /**
     * Check if the entity is currently in a 1-block tall space (crawl space)
     */
    private boolean isInCrawlSpace() {
        BlockPos mobPos = this.mob.blockPosition();
        BlockPos headPos = mobPos.above(1);
        BlockState headBlock = this.mob.level().getBlockState(headPos);
        return !headBlock.isAir() && !headBlock.getCollisionShape(this.mob.level(), headPos).isEmpty();
    }

    /**
     * Make the entity crawl through a 1-block gap
     */
    private void performCrawling() {
        if (!isCrawling) {
            // Start crawling by setting pose to SWIMMING (crawling pose in Minecraft)
            this.mob.setPose(Pose.SWIMMING);
            isCrawling = true;
        }
        
        // Move towards the target while crawling
        double baseSpeed = this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
        double crawlSpeed = baseSpeed * 0.7; // Slightly slower when crawling
        
        // Continue pathfinding towards target
        Path path = this.mob.getNavigation().createPath(this.target, 0);
        if (path != null && path.canReach()) {
            this.mob.getNavigation().moveTo(path, crawlSpeed);
        } else {
            // If no path, move directly towards target
            this.mob.getNavigation().moveTo(this.target, crawlSpeed);
        }
        
        // Look at target
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
    }
}

