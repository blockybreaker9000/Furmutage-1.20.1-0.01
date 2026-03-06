package net.jerika.furmutage.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.pathfinder.Path;

/**
 * MeleeAttackGoal that uses the entity's MOVEMENT_SPEED attribute when pathing to the target,
 * so changing the attribute (e.g. in createAttributes()) actually affects chase speed.
 */
public class AttributeSpeedMeleeAttackGoal extends MeleeAttackGoal {

    public AttributeSpeedMeleeAttackGoal(PathfinderMob mob, boolean followTargetEvenIfNotSeen) {
        // Pass 1.0 as placeholder; we use the attribute in tick() instead
        super(mob, 1.0D, followTargetEvenIfNotSeen);
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distToEnemySqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        this.checkAndPerformAttack(target, distToEnemySqr);
        // Use entity's MOVEMENT_SPEED attribute so modifiers actually affect chase speed
        double speed = this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
        Path path = this.mob.getNavigation().createPath(target, 0);
        if (path != null) {
            this.mob.getNavigation().moveTo(path, speed);
        } else {
            this.mob.getNavigation().moveTo(target, speed);
        }
    }
}
