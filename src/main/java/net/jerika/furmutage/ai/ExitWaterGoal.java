package net.jerika.furmutage.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.EnumSet;

/**
 * Goal that helps entities pathfind out of water by finding the nearest land block
 * and moving towards it when the entity is in water.
 */
public class ExitWaterGoal extends Goal {
    private final PathfinderMob mob;
    private final double speedModifier;
    private BlockPos targetPos;
    private int searchCooldown;

    public ExitWaterGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.searchCooldown = 0;
    }

    @Override
    public boolean canUse() {
        // Only activate if the entity is in water
        if (!this.mob.isInWater() && !this.mob.isInLava()) {
            return false;
        }

        // Only search for land periodically (every 20 ticks = 1 second)
        if (this.searchCooldown > 0) {
            this.searchCooldown--;
            return this.targetPos != null && isValidTarget(this.targetPos);
        }

        // Search for nearest land block
        this.targetPos = findNearestLand();
        this.searchCooldown = 20;

        return this.targetPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue if still in water and we have a valid target
        if (!this.mob.isInWater() && !this.mob.isInLava()) {
            return false;
        }

        // Re-search periodically
        if (this.searchCooldown <= 0) {
            BlockPos newTarget = findNearestLand();
            if (newTarget != null) {
                this.targetPos = newTarget;
            }
            this.searchCooldown = 20;
        } else {
            this.searchCooldown--;
        }

        return this.targetPos != null && isValidTarget(this.targetPos);
    }

    @Override
    public void start() {
        if (this.targetPos != null) {
            this.mob.getNavigation().moveTo(this.targetPos.getX() + 0.5D, this.targetPos.getY(), this.targetPos.getZ() + 0.5D, this.speedModifier);
        }
    }

    @Override
    public void tick() {
        if (this.targetPos != null && this.mob.getNavigation().isDone()) {
            // If navigation stopped, try to find a new target
            BlockPos newTarget = findNearestLand();
            if (newTarget != null) {
                this.targetPos = newTarget;
                this.mob.getNavigation().moveTo(this.targetPos.getX() + 0.5D, this.targetPos.getY(), this.targetPos.getZ() + 0.5D, this.speedModifier);
            }
        }
    }

    /**
     * Finds the nearest land block (non-water, solid block with air above) within search radius.
     */
    private BlockPos findNearestLand() {
        BlockPos entityPos = this.mob.blockPosition();
        int searchRadius = 16; // Search in 16 block radius
        int searchHeight = 10; // Search up to 10 blocks vertically

        BlockPos closestLand = null;
        double closestDistance = Double.MAX_VALUE;

        // Search in expanding circles
        for (int radius = 1; radius <= searchRadius; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    // Only check blocks on the edge of the current radius to avoid redundant checks
                    if (Math.abs(x) != radius && Math.abs(z) != radius && radius > 1) {
                        continue;
                    }

                    for (int y = -searchHeight; y <= searchHeight; y++) {
                        BlockPos checkPos = entityPos.offset(x, y, z);

                        if (isValidLandPosition(checkPos)) {
                            double distance = entityPos.distSqr(checkPos);
                            if (distance < closestDistance) {
                                closestLand = checkPos;
                                closestDistance = distance;
                            }
                        }
                    }
                }
            }

            // If we found land at this radius, return it (closest land)
            if (closestLand != null) {
                return closestLand;
            }
        }

        return closestLand;
    }

    /**
     * Checks if a block position is valid land (solid block with air above, not water).
     */
    private boolean isValidLandPosition(BlockPos pos) {
        BlockState blockState = this.mob.level().getBlockState(pos);
        BlockState aboveState = this.mob.level().getBlockState(pos.above());

        // Check if the block is solid and the block above is air or replaceable
        boolean isSolid = blockState.blocksMotion() && !blockState.getFluidState().is(Fluids.WATER) && !blockState.getFluidState().is(Fluids.LAVA);
        boolean hasAirAbove = aboveState.isAir() || aboveState.canBeReplaced();

        // Also check if there's enough vertical clearance for the entity (at least 3 blocks)
        boolean hasEnoughHeight = true;
        for (int i = 1; i <= 3; i++) {
            BlockState checkAbove = this.mob.level().getBlockState(pos.above(i));
            if (!checkAbove.isAir() && !checkAbove.canBeReplaced()) {
                hasEnoughHeight = false;
                break;
            }
        }

        return isSolid && hasAirAbove && hasEnoughHeight;
    }

    /**
     * Checks if the target position is still valid.
     */
    private boolean isValidTarget(BlockPos pos) {
        return pos != null && isValidLandPosition(pos);
    }
}

