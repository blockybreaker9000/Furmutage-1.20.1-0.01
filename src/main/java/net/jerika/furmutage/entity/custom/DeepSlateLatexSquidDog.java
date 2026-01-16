package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.ChangedStyleLeapAtTargetGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractAquaticEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexSquidDog;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class DeepSlateLatexSquidDog extends AbstractLatexSquidDog {
    public DeepSlateLatexSquidDog(EntityType<? extends DeepSlateLatexSquidDog> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.setMaxUpStep(2.0F); // Increased step height for climbing
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Changed mod style AI goals - matching priority order with other Furmutage entities
        // Priority 1: Melee attack goal
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        
        // Priority 2: Improved pathfinding for jumping onto blocks
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        
        // Priority 3: Random stroll
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 120, false));
        
        // Priority 4: Leap at target (only when target is above)
        this.goalSelector.addGoal(4, new ChangedStyleLeapAtTargetGoal(this, 0.4f));
        
        // Target priorities - Changed mod style
        // Priority 1: Hurt by target
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        
        // Priority 2: Target players
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D) // Increased follow range
                .add(Attributes.ATTACK_DAMAGE, 6.0D) // Increased attack damage
                .add(Attributes.MOVEMENT_SPEED, 0.35D); // Faster movement
    }

    // Spawn rules that allow non-natural spawning (spawn eggs, /summon)
    public static boolean checkDeepSlateSpawnRules(EntityType<DeepSlateLatexSquidDog> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        // Allow spawning via spawn eggs and other non-natural methods
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        // Must be between Y level 0 and -60
        int y = pos.getY();
        if (y > 0 || y < -60) {
            return false;
        }
        // For natural spawning, use simplified aquatic spawn rules (water-based)
        // This is a simplified version that works with RandomSource
        if (!world.getFluidState(pos.below()).is(net.minecraft.tags.FluidTags.WATER)) {
            return false;
        }
        return world.getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER);
    }
}
