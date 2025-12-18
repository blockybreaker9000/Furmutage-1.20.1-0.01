package net.jerika.furmutage.ai.mugling;

import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.EnumSet;

public class MuglingGroupGoal extends Goal {
    private final MuglingEntity mob;
    private MuglingEntity groupLeader;
    private int followTime;
    private final double speedModifier;
    private final float stopDistance;
    private final float startDistance;

    public MuglingGroupGoal(MuglingEntity mob, double speedModifier, float stopDistance, float startDistance) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance * stopDistance;
        this.startDistance = startDistance * startDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isBaby()) {
            return false; // Babies follow parents instead
        }
        
        List<MuglingEntity> list = this.mob.level().getEntitiesOfClass(MuglingEntity.class, 
                this.mob.getBoundingBox().inflate(12.0D, 6.0D, 12.0D));
        
        MuglingEntity nearestMugling = null;
        double nearestDistance = Double.MAX_VALUE;

        for (MuglingEntity mugling : list) {
            if (mugling != this.mob && !mugling.isBaby()) {
                double distance = this.mob.distanceToSqr(mugling);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestMugling = mugling;
                }
            }
        }

        if (nearestMugling == null) {
            return false;
        } else if (nearestDistance < this.stopDistance) {
            return false; // Already close enough
        } else {
            this.groupLeader = nearestMugling;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.groupLeader == null || !this.groupLeader.isAlive()) {
            return false;
        }
        
        double distance = this.mob.distanceToSqr(this.groupLeader);
        return distance >= this.stopDistance && distance <= this.startDistance;
    }

    @Override
    public void start() {
        this.followTime = 0;
    }

    @Override
    public void stop() {
        this.groupLeader = null;
    }

    @Override
    public void tick() {
        if (this.groupLeader == null) {
            return;
        }
        
        this.followTime++;
        
        if (this.mob.distanceToSqr(this.groupLeader) < this.stopDistance) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.groupLeader, this.speedModifier);
        }
    }
}

