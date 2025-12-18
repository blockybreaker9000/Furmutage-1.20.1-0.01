package net.jerika.furmutage.ai.latex_beast_ai;

import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class MutantFamilyAi extends MeleeAttackGoal {
    private final LatexMutantFamilyEntity entity;
    private int attackDelay = 5;
    private int ticksUntilNextAttack = 5;
    private boolean shouldCountTillNextAttack = false;

    public MutantFamilyAi(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((LatexMutantFamilyEntity) pMob);
    }
    @Override
    public void start() {
        super.start();
        attackDelay = 5;
        ticksUntilNextAttack = 5;
    }
    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setMutantFamilyAttack(true);
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setMutantFamilyAttack(false);
            entity.attackAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        // Shorter attack range - 1.5 blocks instead of default ~2.0 blocks
        double attackReach = this.mob.getBbWidth() * 2.0F + 1.5D;
        return pDistToEnemySqr <= attackReach * attackReach;
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }


    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
        // Flag will be reset by client-side animation timeout or when enemy moves out of range
    }

    @Override
    public void tick() {
        super.tick();
        if(shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        entity.setMutantFamilyAttack(false);
        super.stop();
    }
}


