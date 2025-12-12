package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.TSCDroneBulletProjectile;
import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;

public class DroneRangedAttackGoal extends RangedAttackGoal {
    private final TSCDroneEntity drone;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;

    public DroneRangedAttackGoal(TSCDroneEntity drone, double speedModifier, int attackInterval, float attackRadius) {
        super((RangedAttackMob) drone, speedModifier, attackInterval, attackRadius);
        this.drone = drone;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackInterval;
        this.attackIntervalMax = attackInterval;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
    }

    @Override
    public boolean canUse() {
        return this.drone.getTarget() != null && this.isHoldingProjectile();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.drone.getNavigation().isDone();
    }

    @Override
    public void start() {
        super.start();
        this.drone.setAggressive(true);
        this.attackTime = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.drone.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
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

        if (distanceSqr <= (double)this.attackRadiusSqr && this.seeTime >= 5) {
            this.drone.getNavigation().stop();
        } else {
            this.drone.getNavigation().moveTo(target, this.speedModifier);
        }

        this.drone.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (--this.attackTime == 0) {
            if (!canSee) {
                return;
            }

            float f = (float)Math.sqrt(distanceSqr) / this.attackRadius;
            float velocity = net.minecraft.util.Mth.clamp(f, 0.1F, 1.0F);
            this.performRangedAttack(target, velocity);
            this.attackTime = net.minecraft.util.Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = net.minecraft.util.Mth.floor(net.minecraft.util.Mth.lerp(Math.sqrt(distanceSqr) / (double)this.attackRadius, (double)this.attackIntervalMin, (double)this.attackIntervalMax));
        }
    }

    protected void performRangedAttack(LivingEntity target, float velocity) {
        TSCDroneBulletProjectile bullet = new TSCDroneBulletProjectile(this.drone.level(), this.drone);
        double d0 = target.getX() - this.drone.getX();
        double d1 = target.getY(0.3333333333333333D) - bullet.getY();
        double d2 = target.getZ() - this.drone.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        bullet.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.drone.level().getDifficulty().getId() * 4));
        this.drone.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.drone.getRandom().nextFloat() * 0.4F + 0.8F));
        this.drone.level().addFreshEntity(bullet);
    }

    protected boolean isHoldingProjectile() {
        return true; // Drone always has bullets
    }
}

