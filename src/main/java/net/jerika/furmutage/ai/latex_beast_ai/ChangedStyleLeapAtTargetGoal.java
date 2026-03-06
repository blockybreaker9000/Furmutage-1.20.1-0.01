package net.jerika.furmutage.ai.latex_beast_ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;

/**
 * LeapAtTargetGoal that matches Changed mod's behavior:
 * Only leaps when the target is above the entity and within 3 blocks horizontal distance.
 */
public class ChangedStyleLeapAtTargetGoal extends LeapAtTargetGoal {
    private static final double MAX_HORIZONTAL_LEAP_DISTANCE = 3.0D;
    private static final double MAX_HORIZONTAL_LEAP_DISTANCE_SQR = MAX_HORIZONTAL_LEAP_DISTANCE * MAX_HORIZONTAL_LEAP_DISTANCE;

    private final PathfinderMob mob;
    private int lastLeapTick = Integer.MIN_VALUE;

    public ChangedStyleLeapAtTargetGoal(PathfinderMob mob, float leapStrength) {
        // Slightly lower leap strength than vanilla to avoid huge jumps above the player.
        super(mob, leapStrength * 0.7f);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        }
        // Only leap when target is above us
        if (target.position().y() <= this.mob.position().y()) {
            return false;
        }
        // Only leap when target is within 3 blocks horizontal distance
        double dx = target.getX() - this.mob.getX();
        double dz = target.getZ() - this.mob.getZ();
        double horizontalDistSqr = dx * dx + dz * dz;
        if (horizontalDistSqr > MAX_HORIZONTAL_LEAP_DISTANCE_SQR) {
            return false;
        }
        // 2000-tick (~100s) cooldown between leaps so it doesn't stack with pathfinding jumps
        if (this.mob.tickCount - this.lastLeapTick < 2000) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.lastLeapTick = this.mob.tickCount;
    }
}

