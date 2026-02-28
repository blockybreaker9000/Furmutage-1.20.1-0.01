package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.ExitWaterGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.core.BlockPos;

/**
 * Two-headed white latex mutant. Spawns in Nether, 50 health, uses HumanoidAnimator.
 */
public class TwoHeadedWhiteLatexMutantUnifiedEntity extends ChangedEntity {

    public TwoHeadedWhiteLatexMutantUnifiedEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ExitWaterGoal(this, 0.8D));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new ChangedEntityImprovedPathfindingGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    public static boolean checkSpawnRules(EntityType<TwoHeadedWhiteLatexMutantUnifiedEntity> type,
                                         ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) return true;
        if (level.getLevel().dimension() != Level.NETHER) return false;
        return net.minecraft.world.entity.monster.Monster.checkMonsterSpawnRules(type, level, reason, pos, random);
    }
}
