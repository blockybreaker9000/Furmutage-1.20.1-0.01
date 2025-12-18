package net.jerika.furmutage.ai.tsc_drone_ai;

import net.jerika.furmutage.entity.projectiles.TSCShockGrenadeProjectile;
import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DroneGrenadeAttackGoal extends Goal {
    private final TSCDroneEntity drone;
    private final double speedModifier;
    private final int attackInterval;
    private int attackTime = 0;
    private int seeTime = 0;

    public DroneGrenadeAttackGoal(TSCDroneEntity drone, double speedModifier, int attackInterval) {
        this.drone = drone;
        this.speedModifier = speedModifier;
        this.attackInterval = attackInterval;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.drone.getTarget();
        return target != null && target.isAlive() && this.drone.canAttack(target);
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.drone.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.attackTime = 0;
        this.seeTime = 0;
    }

    @Override
    public void stop() {
        this.seeTime = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.drone.getTarget();
        if (target == null) {
            return;
        }

        double distanceSqr = this.drone.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean canSee = this.drone.getSensing().hasLineOfSight(target);

        if (canSee) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        // Move towards target
        if (distanceSqr > 64.0D) { // Keep distance of 8 blocks
            this.drone.getNavigation().moveTo(target, this.speedModifier);
        } else {
            this.drone.getNavigation().stop();
        }

        this.drone.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Attack every attackInterval ticks (20 seconds = 400 ticks)
        if (++this.attackTime >= this.attackInterval) {
            if (canSee) {
                this.performGrenadeAttack(target);
                this.attackTime = 0;
            }
        }
    }

    protected void performGrenadeAttack(LivingEntity target) {
        TSCShockGrenadeProjectile grenade = new TSCShockGrenadeProjectile(this.drone.level(), this.drone);
        double d0 = target.getX() - this.drone.getX();
        double d1 = target.getY(0.3333333333333333D) - grenade.getY();
        double d2 = target.getZ() - this.drone.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        grenade.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 0.8F, (float)(14 - this.drone.level().getDifficulty().getId() * 4));
        this.drone.playSound(SoundEvents.SPLASH_POTION_THROW, 1.0F, 1.0F / (this.drone.getRandom().nextFloat() * 0.4F + 0.8F));
        this.drone.level().addFreshEntity(grenade);
    }
}

