package net.jerika.furmutage.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

/**
 * Custom target goal that allows entities to detect players from far away
 * if they have line of sight. Uses a much larger detection range than default.
 */
public class LongRangePlayerTargetGoal extends NearestAttackableTargetGoal<Player> {
    private static final double DETECTION_RANGE = 128.0; // Very long range (128 blocks)
    private final TargetingConditions longRangeTargeting;
    
    public LongRangePlayerTargetGoal(PathfinderMob mob) {
        super(mob, Player.class, 10, true, true, null);
        // Create custom targeting conditions with long range
        this.longRangeTargeting = TargetingConditions.forCombat()
                .range(DETECTION_RANGE)
                .selector((entity) -> {
                    if (!(entity instanceof Player)) {
                        return false;
                    }
                    // Only target if we have line of sight
                    return mob.getSensing().hasLineOfSight(entity);
                });
    }
    
    @Override
    protected void findTarget() {
        // Use custom targeting conditions with long range
        this.target = this.mob.level().getNearestPlayer(
                this.longRangeTargeting,
                this.mob,
                this.mob.getX(),
                this.mob.getEyeY(),
                this.mob.getZ()
        );
    }
}

