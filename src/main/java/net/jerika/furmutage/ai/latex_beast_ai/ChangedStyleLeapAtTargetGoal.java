package net.jerika.furmutage.ai.latex_beast_ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;

/**
 * LeapAtTargetGoal that matches Changed mod's behavior:
 * Only leaps when the target is above the entity.
 */
public class ChangedStyleLeapAtTargetGoal extends LeapAtTargetGoal {
    private final PathfinderMob mob;

    public ChangedStyleLeapAtTargetGoal(PathfinderMob mob, float p_25426_) {
        super(mob, p_25426_);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target != null && target.position().y() > this.mob.position().y) {
            return super.canUse();
        }
        return false;
    }
}

