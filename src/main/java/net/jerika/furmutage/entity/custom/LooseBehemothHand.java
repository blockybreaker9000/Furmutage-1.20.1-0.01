package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.AttributePresets;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

public class LooseBehemothHand extends ChangedEntity implements GenderedEntity {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();

    public LooseBehemothHand(EntityType<? extends LooseBehemothHand> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6) {
            if (!this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(this.tickCount);
            }
            this.walkAnimationState.stop();
        } else {
            this.idleAnimationState.stop();
            if (!this.walkAnimationState.isStarted()) {
                this.walkAnimationState.start(this.tickCount);
            }
        }
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        AttributePresets.catLike(attributes);
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(32.0D);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.25, 80, false));
        if (GoalUtils.hasGroundPathNavigation(this)) {
            this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        }
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 200;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#dddddd");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes();
    }

    /** Spawn deep underground at deepslate level or below (Y <= 8). */
    public static boolean checkLooseBehemothHandSpawnRules(EntityType<LooseBehemothHand> entityType, ServerLevelAccessor world, MobSpawnType reason, net.minecraft.core.BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        int y = pos.getY();
        if (y > 8) {
            return false;
        }
        if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(50)) {
            return false;
        }
        if (world.getBrightness(LightLayer.BLOCK, pos) > 0) {
            return false;
        }
        if (ChangedEntity.getLevelBrightnessAt(world.getLevel(), pos) > random.nextInt(12)) {
            return false;
        }
        if (random.nextFloat() > 0.4f) {
            return false;
        }
        return net.minecraft.world.entity.monster.Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }
}
