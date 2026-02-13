package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedStyleLeapAtTargetGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

public class LatexBloodWormMutant extends ChangedEntity {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(LatexBloodWormMutant.class, EntityDataSerializers.BYTE);
    
    public LatexBloodWormMutant(EntityType<? extends LatexBloodWormMutant> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(2.0F); // Increased step height for climbing
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            // Improved climbing detection - only when actively moving into a wall
            boolean canClimb = false;
            Vec3 movement = this.getDeltaMovement();
            boolean isMovingForward = Math.abs(movement.x) > 0.01 || Math.abs(movement.z) > 0.01;
            
            // Only climb if we're hitting a wall AND moving forward
            if (this.horizontalCollision && isMovingForward) {
                canClimb = true;
            } else if (this.getTarget() != null && this.getTarget().getY() > this.getY() + 1.0 && isMovingForward) {
                // Target is above, try to climb towards it
                BlockPos pos = this.blockPosition();
                Direction facing = Direction.fromYRot(this.getYRot());
                BlockPos frontPos = pos.relative(facing);
                BlockState frontState = this.level().getBlockState(frontPos);
                if (!frontState.isAir() && frontState.getCollisionShape(this.level(), frontPos) != net.minecraft.world.phys.shapes.Shapes.empty()) {
                    canClimb = true;
                }
            }
            
            this.setClimbing(canClimb);
        }
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }
        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(128.0D); // Much longer follow range for persistent targeting
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.8);
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(26);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Changed mod style AI goals - matching priority order with other Furmutage entities
        // Priority 1: Melee attack goal
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.6D, false));
        
        // Priority 2: Improved pathfinding for jumping onto blocks
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        
        // Priority 3: Random stroll
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 120, false));
        
        // Priority 4: Leap at target (only when target is above)
        this.goalSelector.addGoal(4, new ChangedStyleLeapAtTargetGoal(this, 0.4f));
        
        // Priority 5: Open doors (if has ground navigation)
        if (GoalUtils.hasGroundPathNavigation(this))
            this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        
        // Priority 7: Look at player
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 7.0F));
        
        // Priority 8: Random look around
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        
        // Priority 9: Look at villager
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Villager.class, 7.0F, 0.2F));
        
        // Target priorities - Changed mod style
        // Priority 1: Hurt by target
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        // Priority 2: Target players
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        
        // Priority 3: Target humanoid entities (skeletons, zombies, pillagers, villagers)
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Raider.class, true)); // Pillagers are Raiders
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public boolean isMovingSlowly() {
        return this.isCrouching();
    }

    @Override
    public double getMyRidingOffset() {
        return -0.1175;
    }

    @Override
    public @NotNull EntityShape getEntityShape() {
        return EntityShape.NAGA;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        // Instantly transfur specific humanoid entities into this entity type
        if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player) && !this.level().isClientSide) {
            if ((entity instanceof Skeleton || entity instanceof Zombie || entity instanceof Raider || entity instanceof Villager)
                    && TransfurVariant.getEntityVariant(livingEntity) == null) {
                
                // Try to use TransfurVariant first if available
                TransfurVariant<?> variant = this.getSelfVariant();
                if (variant != null) {
                    TransfurContext context = TransfurContext.npcLatexHazard(this, TransfurCause.GRAB_REPLICATE);
                    ProcessTransfur.transfur(livingEntity, this.level(), variant, false, context);
                    return true;
                } else {
                    // If no variant, directly spawn the entity type and replace
                    Entity createdEntity = this.getType().create(this.level());
                    if (createdEntity instanceof ChangedEntity newEntity) {
                        newEntity.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 
                                        livingEntity.getYRot(), livingEntity.getXRot());
                        if (livingEntity.hasCustomName()) {
                            newEntity.setCustomName(livingEntity.getCustomName());
                            newEntity.setCustomNameVisible(livingEntity.isCustomNameVisible());
                        }
                        this.level().addFreshEntity(newEntity);
                        livingEntity.discard();
                        return true;
                    }
                }
            }
        }
        
        // Use default transfur behavior for other entities
        if (!tryTransfurTarget(entity))
            return super.doHurtTarget(entity);
        else
            return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes();
    }

    // Custom spawn rule - must be between Y level -20 and -64, in darkness
    public static boolean checkLatexBloodWormMutantSpawnRules(EntityType<LatexBloodWormMutant> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        if (world instanceof net.minecraft.server.level.WorldGenRegion) {
            return false;
        }

        int y = pos.getY();
        if (y > -20 || y < -64) {
            return false;
        }
        if (world.getBrightness(net.minecraft.world.level.LightLayer.SKY, pos) > random.nextInt(50)) {
            return false;
        }
        if (world.getBrightness(net.minecraft.world.level.LightLayer.BLOCK, pos) > 0) {
            return false;
        }

        return net.minecraft.world.entity.monster.Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }
}
