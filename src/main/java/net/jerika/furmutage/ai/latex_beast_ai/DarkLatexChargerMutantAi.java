package net.jerika.furmutage.ai.latex_beast_ai;

import net.jerika.furmutage.entity.custom.DarkLatexChargerMutantEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DarkLatexChargerMutantAi extends MeleeAttackGoal {
    private static final int ATTACK_COOLDOWN_TICKS = 20; // 1 second
    private static final int ANIMATION_LEAD_TICKS = 10;  // Ticks before hit to start attack animation

    private final DarkLatexChargerMutantEntity entity;
    private int ticksUntilNextAttack = ATTACK_COOLDOWN_TICKS;
    private boolean shouldCountTillNextAttack = false;

    public DarkLatexChargerMutantAi(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = (DarkLatexChargerMutantEntity) pMob;
    }

    @Override
    public void start() {
        super.start();
        ticksUntilNextAttack = ATTACK_COOLDOWN_TICKS;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if (isTimeToStartAttackAnimation()) {
                entity.setChargerAttack(true);
            }

            if (isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setChargerAttack(false);
            entity.attackAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        double attackReach = this.mob.getBbWidth() * 2.0F + 4.0D;
        return pDistToEnemySqr <= attackReach * attackReach;
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(ATTACK_COOLDOWN_TICKS);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= ANIMATION_LEAD_TICKS;
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        // AOE melee - hit all valid targets in range
        double aoeRadius = 4.0D;
        AABB aoeBox = this.mob.getBoundingBox().inflate(aoeRadius, 2.0, aoeRadius);
        if (this.mob.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    aoeBox,
                    e -> e != this.mob && e.isAlive() && !this.mob.isAlliedTo(e) && this.mob.canAttack(e)
            );
            for (LivingEntity target : targets) {
                this.mob.doHurtTarget(target);
            }
        }
        entity.setChargerAttack(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        entity.setChargerAttack(false);
        super.stop();
    }
}
