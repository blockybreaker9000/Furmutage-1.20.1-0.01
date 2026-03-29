package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.tags.FluidTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Extended creeper-style bomber that uses latex mutant animations.
 */
public class LatexMutantBomberEntity extends Creeper {
    
    public LatexMutantBomberEntity(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        // Idle animation: play continuously when stationary
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6) {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
            }
        } else {
            this.idleAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        // Start from creeper defaults, then tweak for mutant stats
        AttributeSupplier.Builder builder = Creeper.createAttributes();
        builder.add(Attributes.MAX_HEALTH, 30.0D);
        builder.add(Attributes.MOVEMENT_SPEED, 0.35D);
        builder.add(Attributes.FOLLOW_RANGE, 16.0D);
        builder.add(Attributes.ATTACK_DAMAGE, 0.5D);
        builder.add(Attributes.ARMOR, 2.0D);
        builder.add(Attributes.ATTACK_KNOCKBACK, 0.0D);
        return builder;
    }

    /**
     * Custom spawn rules so the bomber:
     * - Only naturally spawns at night
     * - Never spawns in water (no ocean/river floor spawns)
     */
    public static boolean checkLatexMutantBomberSpawnRules(EntityType<LatexMutantBomberEntity> type,
                                                           ServerLevelAccessor level,
                                                           MobSpawnType reason,
                                                           BlockPos pos,
                                                           RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true; // allow eggs/commands
        }
        if (level instanceof WorldGenRegion) {
            return false;
        }
        // Don't spawn in water or with water directly above
        if (level.getFluidState(pos).is(FluidTags.WATER) || level.getFluidState(pos.above()).is(FluidTags.WATER)) {
            return false;
        }

        // Use standard monster rules first (valid floor, mob cap, etc.)
        if (!Monster.checkMonsterSpawnRules(type, level, reason, pos, random)) {
            return false;
        }

        long dayTime = level.getLevelData().getDayTime() % 24000L;
        boolean isNight = dayTime >= 13000L && dayTime < 23000L;
        if (!isNight) {
            return false;
        }

        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return blockLight <= 7 && skyLight <= 7;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.LATEX_MUTANT_BOMBER_AMBIENT.get();
    }

    @Override
    protected float getSoundVolume() {
        // Make the bomber's ambient more subtle
        return super.getSoundVolume() * 0.5f;
    }

    @Override
    public int getAmbientSoundInterval() {
        // Play bomber ambient less frequently
        return super.getAmbientSoundInterval() / 2;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.LATEX_MUTANT_BOMBER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.LATEX_MUTANT_BOMBER_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // Resistant to explosions - reduce explosion damage by 90%
        if (pSource.getMsgId().contains("explosion") || 
            pSource.getDirectEntity() != null && pSource.getDirectEntity().getClass().getSimpleName().contains("Explosion")) {
            // Only take 10% of explosion damage
            return super.hurt(pSource, pAmount * 0.1f);
        }
        return super.hurt(pSource, pAmount);
    }
}

