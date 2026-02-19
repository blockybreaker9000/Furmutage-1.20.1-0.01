package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedStyleLeapAtTargetGoal;
import net.jerika.furmutage.ai.latex_beast_ai.MutantFamilyAi;
import net.jerika.furmutage.sound.ModSounds;
import net.ltxprogrammer.changed.entity.beast.WhiteLatexEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexMutantFamilyEntity extends Monster {
    public LatexMutantFamilyEntity(EntityType<? extends Monster> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        @Nullable LivingEntity target;
        // Set higher step height to help entity step over blocks more easily
        this.setMaxUpStep(1.0f);
    }




    private static final EntityDataAccessor<Boolean> MUTANT_FAMILY_ATTACK =
            SynchedEntityData.defineId(LatexMutantFamilyEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        } else {
            if (this.tickCount % 5 == 0) {
                destroyNearbyLeaves();
            }
        }
    }

    /** Leaf breaking AI (same as giant): destroy leaves within 1 block of hitbox. */
    private void destroyNearbyLeaves() {
        AABB boundingBox = this.getBoundingBox().inflate(1.0);
        BlockPos minPos = BlockPos.containing(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        BlockPos maxPos = BlockPos.containing(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    BlockState blockState = this.level().getBlockState(checkPos);
                    if (blockState.is(BlockTags.LEAVES)) {
                        this.level().destroyBlock(checkPos, true);
                    }
                }
            }
        }
    }

    private void setupAnimationStates() {
        // Idle animation: play continuously when stationary
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6) {
            if (!this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(this.tickCount);
            }
        } else {
            this.idleAnimationState.stop();
        }

        // Attack animation
        if(this.ismutantFamilyAttack() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 20; //animation Length
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if(!this.ismutantFamilyAttack() && attackAnimationTimeout <= 0) {
            attackAnimationState.stop();
        }
    }
    private static final int MOB_FLAG_AGGRESSIVE = 4;

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }

    public void setMutantFamilyAttack(boolean mutantFamilyAttack) {
        this.entityData.set(MUTANT_FAMILY_ATTACK, mutantFamilyAttack);
    }


    public boolean ismutantFamilyAttack() {
        return this.entityData.get(MUTANT_FAMILY_ATTACK);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MUTANT_FAMILY_ATTACK, false);
    }

    @Override
    protected void registerGoals(){
        // Changed mod style AI goals - matching priority order
        // Priority 1: Custom attack goal (MutantFamilyAi replaces MeleeAttackGoal)
        this.goalSelector.addGoal(1, new MutantFamilyAi(this, 3.0, true));
        
        // Priority 2: Improved pathfinding for jumping onto blocks
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        
        // Priority 3: Random stroll
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 120, false));
        
        // Priority 4: Leap at target (only when target is above)
        this.goalSelector.addGoal(4, new ChangedStyleLeapAtTargetGoal(this, 0.4f));
        
        // Priority 5: Open doors (if has ground navigation)
        if (GoalUtils.hasGroundPathNavigation(this))
            this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        
        // Priority 6: Float in water
        this.goalSelector.addGoal(6, new FloatGoal(this));
        
        // Priority 7: Look at player
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 7.0F));
        
        // Priority 8: Random look around
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        
        // Priority 9: Look at villager
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Villager.class, 7.0F, 0.2F));
        
        // Target priorities - Changed mod style
        // Priority 1: Hurt by target (alert white latex entities)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WhiteLatexEntity.class).setAlertOthers());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        
        // Priority 2: Target players
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        
        // Priority 3: Target villagers
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true, false));
        
        // Priority 4: Target iron golems and dark latex
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, false));
    }
    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 400)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ARMOR_TOUGHNESS, 10)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.FOLLOW_RANGE, 60.0)
                .add(Attributes.JUMP_STRENGTH, 5.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 5.0);
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.LATEX_MUTANT_FAMILY_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.LATEX_MUTANT_FAMILY_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.LATEX_MUTANT_FAMILY_DEATH.get();
    }

    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        LivingEntity previousTarget = this.getTarget();
        super.setTarget(pTarget);
        if (!this.level().isClientSide() && pTarget != null && previousTarget == null) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    ModSounds.LATEX_MUTANT_FAMILY_TARGETS.get(), this.getSoundSource(), 1.0f, 1.0f);
        }
    }

    @Override
    public boolean fireImmune() {
        return true; // Fire and lava resistant
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // Cancel fire and lava damage
        if (pSource == this.level().damageSources().onFire() ||
            pSource == this.level().damageSources().inFire() ||
            pSource == this.level().damageSources().lava() ||
            pSource.getMsgId().contains("fire") ||
            pSource.getMsgId().contains("lava")) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.0f; // No water slowdown - fast in water
    }

    /** Spawn underground only (Y 10 to -64), same style as Deep Cave Hypno Cat – vanilla brightness only. */
    public static boolean checkMutantFamilySpawnRules(EntityType<LatexMutantFamilyEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        if (world instanceof WorldGenRegion) {
            return false;
        }
        int y = pos.getY();
        if (y > 10 || y < -64) {
            return false;
        }
        if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(50)) {
            return false;
        }
        if (world.getBrightness(LightLayer.BLOCK, pos) > 0) {
            return false;
        }
        if (random.nextFloat() > 0.5f) {
            return false;
        }
        return Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }

    @Override
    public void travel(net.minecraft.world.phys.Vec3 pTravelVector) {
        if (this.isEffectiveAi() && (this.isInWater() || this.isInLava())) {
            // Increase movement speed in water and lava
            float speedMultiplier = this.isInLava() ? 1.3f : 1.0f; // Slightly faster in lava
            this.moveRelative(speedMultiplier, pTravelVector);
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(this.isInLava() ? 1.2 : 1.0));
        } else {
            super.travel(pTravelVector);
        }
    }

}

