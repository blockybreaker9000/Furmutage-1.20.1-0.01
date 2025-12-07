package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import java.util.List;

public class MuglingPackAttackGoal extends MeleeAttackGoal {
    private final MuglingEntity mugling;
    private static final int MIN_GROUP_SIZE = 4; // Minimum Muggings needed to attack

    public MuglingPackAttackGoal(MuglingEntity mob, double speedModifier, boolean followTargetEvenIfNotSeen) {
        super(mob, speedModifier, followTargetEvenIfNotSeen);
        this.mugling = mob;
    }

    @Override
    public boolean canUse() {
        // Only attack if we have enough nearby Muggings
        if (!hasEnoughNearbyMuggings()) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        // Continue attacking if we still have enough Muggings nearby
        if (!hasEnoughNearbyMuggings()) {
            return false;
        }
        return super.canContinueToUse();
    }

    private boolean hasEnoughNearbyMuggings() {
        List<MuglingEntity> nearbyMuggings = this.mugling.level().getEntitiesOfClass(
                MuglingEntity.class,
                this.mugling.getBoundingBox().inflate(16.0D, 8.0D, 16.0D),
                (mugling) -> mugling != this.mugling && mugling.isAlive()
        );

        return nearbyMuggings.size() >= MIN_GROUP_SIZE;
    }
}

