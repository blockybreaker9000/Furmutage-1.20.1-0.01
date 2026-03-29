package net.jerika.furmutage.entity.custom;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Pure White Latex Crawler - a smaller surface/cave crawler that uses the
 * Blockbench model + animations. Uses ChangedEntity as a base so it can
 * integrate with latex team logic and transfur systems.
 */
public class PureWhiteLatexCrawlerEntity extends ChangedEntity {

    // Simple always-running idle animation state for the model
    private final AnimationState idleAnimationState = new AnimationState();



    public PureWhiteLatexCrawlerEntity(EntityType<? extends PureWhiteLatexCrawlerEntity> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0F);
    }

    public AnimationState getIdleAnimationState() {
        return idleAnimationState;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            // Keep the idle animation running on the client
            this.idleAnimationState.startIfStopped(this.tickCount);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Base latex attributes; final values are tuned in setAttributes()
        return ChangedEntity.createLatexAttributes();
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        // Explicitly override key stats similar to other Changed-based entities
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(24.0D);
        // Pretty slow movement, roughly "slowness 3" feel
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.18D);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(10.0D);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    /**
     * Spawn rules:
     * - Natural spawns only in the overworld
     * - Surface: anywhere dark enough for monsters, at Y >= 50 (night-time or shadowed spots)
     * - Caves: deep underground at Y <= 0, in darkness
     */
    public static boolean checkPureWhiteLatexCrawlerSpawnRules(EntityType<PureWhiteLatexCrawlerEntity> type,
                                                                ServerLevelAccessor level,
                                                                MobSpawnType reason,
                                                                BlockPos pos,
                                                                RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true; // allow spawn eggs / commands
        }

        // Use vanilla monster checks first (valid floor, mob cap, etc.)
        if (!Monster.checkMonsterSpawnRules(type, level, reason, pos, random)) {
            return false;
        }

        int y = pos.getY();
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);

        // Surface: slightly above sea level, requires low block light and moderate sky light
        if (y >= 50) {
            return blockLight <= 7 && skyLight <= 15;
        }

        // Deep caves: below 0, in full darkness
        if (y <= 0) {
            return blockLight == 0 && skyLight == 0;
        }

        // Between surface and deep caves: don't spawn
        return false;
    }
}

