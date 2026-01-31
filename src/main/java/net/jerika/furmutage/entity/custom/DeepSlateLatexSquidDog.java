package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedStyleLeapAtTargetGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexSquidDog;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DeepSlateLatexSquidDog extends AbstractLatexSquidDog {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(DeepSlateLatexSquidDog.class, EntityDataSerializers.BYTE);
    public DeepSlateLatexSquidDog(EntityType<? extends DeepSlateLatexSquidDog> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
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
        
        // Priority 3: Target humanoid entities (skeletons, zombies, pillagers, villagers)
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Raider.class, true)); // Pillagers are Raiders
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        // Drag players into water when attacking
        if (entity instanceof Player player && !this.level().isClientSide) {
            Vec3 playerPos = player.position();
            BlockPos playerBlockPos = player.blockPosition();
            
            // Find nearby water blocks
            BlockPos nearestWater = null;
            double minDistance = Double.MAX_VALUE;
            
            // Search in a 16-block radius around the player
            for (int x = -16; x <= 16; x++) {
                for (int y = -8; y <= 8; y++) {
                    for (int z = -16; z <= 16; z++) {
                        BlockPos checkPos = playerBlockPos.offset(x, y, z);
                        if (this.level().getFluidState(checkPos).is(FluidTags.WATER) && 
                            this.level().getBlockState(checkPos.below()).is(Blocks.WATER)) {
                            // Found water, check if it's closer than previous
                            Vec3 waterPos = Vec3.atCenterOf(checkPos);
                            double distance = playerPos.distanceToSqr(waterPos);
                            if (distance < minDistance) {
                                minDistance = distance;
                                nearestWater = checkPos;
                            }
                        }
                    }
                }
            }
            
            // If water found, drag the player towards it
            if (nearestWater != null) {
                Vec3 waterPos = Vec3.atCenterOf(nearestWater);
                Vec3 direction = waterPos.subtract(playerPos).normalize();
                // Apply strong pull force towards water
                double pullStrength = 0.3D;
                Vec3 pull = direction.multiply(pullStrength, pullStrength * 0.5, pullStrength);
                player.setDeltaMovement(player.getDeltaMovement().add(pull));
                // Make sure player doesn't fly away - cap vertical velocity
                Vec3 currentMovement = player.getDeltaMovement();
                player.setDeltaMovement(currentMovement.x, Math.min(currentMovement.y, 0.3D), currentMovement.z);
            }
        }
        
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
        return ChangedEntity.createLatexAttributes()
                .add(Attributes.FOLLOW_RANGE, 128.0D) // Much longer follow range for persistent targeting
                .add(Attributes.ATTACK_DAMAGE, 6.0D) // Increased attack damage
                .add(Attributes.MOVEMENT_SPEED, 0.35D); // Faster movement
    }

    // Spawn rules that allow non-natural spawning (spawn eggs, /summon)
    public static boolean checkDeepSlateSpawnRules(EntityType<DeepSlateLatexSquidDog> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        // Allow spawning via spawn eggs and other non-natural methods
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        // Don't run during world gen (can hang at 4%) – natural spawn runs later when world is ServerLevel
        if (world instanceof WorldGenRegion) {
            return false;
        }
        // Only in water blocks, between Y level 10 and -64 (inclusive)
        int y = pos.getY();
        if (y > 10 || y < -64) {
            return false;
        }
        if (!world.getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER)) {
            return false;
        }
        if (!world.getFluidState(pos.below()).is(net.minecraft.tags.FluidTags.WATER)) {
            return false;
        }
        return true;
    }
}
