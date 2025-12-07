package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

import java.util.List;

public class MuglingPackRetaliationGoal extends HurtByTargetGoal {
    private final MuglingEntity mugling;
    private static final int MIN_GROUP_SIZE = 4; // Minimum Muggings needed to attack

    public MuglingPackRetaliationGoal(MuglingEntity mob) {
        super(mob, MuglingEntity.class); // Alert other Muggings when hurt
        this.mugling = mob;
    }

    @Override
    public boolean canUse() {
        // First check if we're hurt
        if (!super.canUse()) {
            return false;
        }

        // Check if we have enough Muggings nearby to form a pack
        return hasEnoughNearbyMuggings();
    }

    @Override
    public boolean canContinueToUse() {
        // Continue if we still have enough Muggings nearby
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

