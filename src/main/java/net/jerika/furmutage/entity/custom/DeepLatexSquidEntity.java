package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.aquatic.GoToWaterGoal;
import net.jerika.furmutage.ai.aquatic.RiseToSurfaceGoal;
import net.jerika.furmutage.ai.aquatic.SinkFromSurfaceGoal;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A copy of the Changed mod's latex squid dog male entity with a different texture.
 * Spawns only in deep underwater caves.
 * Implements aquatic behavior similar to AbstractAquaticEntity from Changed mod.
 */
public class DeepLatexSquidEntity extends PathfinderMob {
    // Reference to the original latex squid dog male entity type for copying behavior
    @Nullable
    private static EntityType<?> originalSquidType = null;
    
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;
    
    // Animation states for client-side rendering
    public final AnimationState swimAnimationState = new AnimationState();
    
    static {
        // Try to get the original latex squid dog male entity type
        try {
            originalSquidType = ForgeRegistries.ENTITY_TYPES.getValue(
                ResourceLocation.tryParse("changed:latex_squid_dog_male")
            );
        } catch (Exception e) {
            furmutage.LOGGER.warn("Could not find latex_squid_dog_male entity type: {}", e.getMessage());
        }
    }
    
    public DeepLatexSquidEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new AquaticMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, pLevel);
        this.groundNavigation = new GroundPathNavigation(this, pLevel);
        this.groundNavigation.setCanOpenDoors(true);
    }
    
    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return this.groundNavigation; // Default to ground, switch to water when needed
    }
    
    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }
    
    /**
     * Copy attributes from the original latex squid dog male entity if available.
     * Otherwise, use default aquatic mob attributes.
     */
    public static AttributeSupplier.Builder createAttributes() {
        // Default attributes for aquatic mob (similar to latex squid)
        AttributeSupplier.Builder builder = AttributeSupplier.builder()
            .add(Attributes.MAX_HEALTH, 30.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.925D) // Matches AbstractLatexSquidDog
            .add(Attributes.ATTACK_DAMAGE, 4.0D)
            .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
        
        // Add Forge attributes if available
        try {
            // Add swim speed attribute
            if (net.minecraftforge.common.ForgeMod.SWIM_SPEED.isPresent()) {
                builder.add(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get(), 1.3D); // Matches AbstractLatexSquidDog
            }
            
            // Add gravity attribute (required for PathfinderMob)
            if (net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.isPresent()) {
                builder.add(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get(), 0.08D); // Default gravity
            }
        } catch (Exception e) {
            furmutage.LOGGER.warn("Could not add Forge attributes: {}", e.getMessage());
        }
        
        return builder;
    }
    
    @Override
    protected void registerGoals() {
        // Aquatic AI goals from AbstractAquaticEntity
        this.goalSelector.addGoal(1, new GoToWaterGoal(this, 0.3));
        this.goalSelector.addGoal(1, new SinkFromSurfaceGoal(this, 0.3));
        this.goalSelector.addGoal(1, new RiseToSurfaceGoal(this, 0.3));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 0.4D, 10));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.player.Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.npc.Villager.class, true));
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.updateSwimming();
        } else {
            // Client-side animation state updates
            if (this.isSwimming() && this.isInWater()) {
                if (!this.swimAnimationState.isStarted()) {
                    this.swimAnimationState.start(this.tickCount);
                }
            } else {
                this.swimAnimationState.stop();
            }
        }
    }
    
    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        boolean animateSwim = this.isInWater() && this.canFitInWater(this.position());
        
        if (this.isEffectiveAi() && animateSwim) {
            this.moveRelative(0.01F, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }
    }
    
    protected float getWaterDepth(BlockPos pos) {
        float depth = 0.0f;
        
        for (int i = 0; i < 3; ++i) {
            BlockState state = this.level().getBlockState(pos.relative(Direction.DOWN, i));
            if (state.getFluidState().is(FluidTags.WATER))
                depth += 1.0f;
            else if (!state.isAir())
                break;
        }
        
        for (int i = 1; i < 3; ++i) {
            BlockState state = this.level().getBlockState(pos.relative(Direction.UP, i));
            if (state.getFluidState().is(FluidTags.WATER))
                depth += 1.0f;
            else if (!state.isAir())
                break;
        }
        
        return depth;
    }
    
    public boolean canFitInWater(Vec3 pos) {
        final float height = this.getDimensions(Pose.STANDING).height;
        final BlockPos originalPos = BlockPos.containing(pos.x, pos.y, pos.z);
        return BlockPos.betweenClosedStream(this.getDimensions(Pose.STANDING).makeBoundingBox(pos).inflate(-0.05))
                .filter(checkPos -> checkPos.getY() == originalPos.getY())
                .allMatch(blockPos -> getWaterDepth(blockPos) >= height);
    }
    
    protected boolean isAirAtEyesWhenStanding(Vec3 pos) {
        final BlockPos originalPos = BlockPos.containing(pos.x, pos.y, pos.z);
        return BlockPos.betweenClosedStream(this.getDimensions(Pose.STANDING).makeBoundingBox(pos).inflate(-0.05))
                .filter(checkPos -> checkPos.getY() > originalPos.getY())
                .allMatch(blockPos -> this.level().getBlockState(blockPos).getFluidState().isEmpty());
    }
    
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            this.setMaxUpStep(this.isInWater() ? 1.05f : 0.7f);
            
            boolean animateSwim = this.isInWater() && this.canFitInWater(this.position());
            
            if (this.isEffectiveAi() && animateSwim) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigation;
                this.setSwimming(false);
            }
            
            if (animateSwim && !(this.wantsToSurface() && this.isAirAtEyesWhenStanding(this.position()))) {
                this.setPose(Pose.SWIMMING);
            } else {
                this.setPose(Pose.STANDING);
            }
        }
    }
    
    public boolean wantsToSwim() {
        LivingEntity livingentity = this.getTarget();
        if (livingentity == null)
            return true;
        if (livingentity.isInWater())
            return true;
        if (livingentity.isPassenger() && livingentity.getVehicle().isInWater())
            return true;
        return false;
    }
    
    public boolean wantsToSurface() {
        return false;
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return true; // Aquatic entity
    }
    
    @Override
    public boolean isPushedByFluid() {
        return false; // Better control in water
    }
    
    /**
     * Aquatic move control similar to AbstractAquaticEntity.AquaticMoveControl
     */
    static class AquaticMoveControl extends MoveControl {
        private final DeepLatexSquidEntity aquaticEntity;
        
        public AquaticMoveControl(DeepLatexSquidEntity pEntity) {
            super(pEntity);
            this.aquaticEntity = pEntity;
        }
        
        @Override
        public void tick() {
            aquaticEntity.updateSwimming();
            
            LivingEntity livingentity = this.aquaticEntity.getTarget();
            if (this.aquaticEntity.isSwimming()) {
                if (livingentity != null && livingentity.getY() > this.aquaticEntity.getY()) {
                    double dx = livingentity.getX() - this.aquaticEntity.getX();
                    double dz = livingentity.getZ() - this.aquaticEntity.getZ();
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > 0.0) {
                        this.aquaticEntity.setDeltaMovement(this.aquaticEntity.getDeltaMovement().add(dx / dist * 0.02D, 0.04D, dz / dist * 0.02D));
                    }
                }
                
                if (this.operation != MoveControl.Operation.MOVE_TO || this.aquaticEntity.getNavigation().isDone()) {
                    this.aquaticEntity.setSpeed(0.0F);
                    return;
                }
                
                double d0 = this.wantedX - this.aquaticEntity.getX();
                double d1 = this.wantedY - this.aquaticEntity.getY();
                double d2 = this.wantedZ - this.aquaticEntity.getZ();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 /= d3;
                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.aquaticEntity.setYRot(this.rotlerp(this.aquaticEntity.getYRot(), f, 90.0F));
                this.aquaticEntity.yBodyRot = this.aquaticEntity.getYRot();
                
                float f1;
                try {
                    if (ForgeMod.SWIM_SPEED.isPresent()) {
                        f1 = (float)(this.speedModifier * this.aquaticEntity.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
                    } else {
                        f1 = (float)(this.speedModifier * this.aquaticEntity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    }
                } catch (Exception e) {
                    f1 = (float)(this.speedModifier * this.aquaticEntity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                }
                
                float f2 = Mth.lerp(0.125F, this.aquaticEntity.getSpeed(), f1);
                this.aquaticEntity.setSpeed(f2 * 1.05f);
                this.aquaticEntity.setDeltaMovement(this.aquaticEntity.getDeltaMovement().add((double)f2 * d0 * 0.005D, (double)f2 * d1 * 0.1D, (double)f2 * d2 * 0.005D));
            } else {
                super.tick();
            }
        }
    }
    
    /**
     * Custom spawn placement for deep underwater caves.
     * Only spawns in water, in caves, at low Y levels (deep underwater).
     */
    public static boolean checkDeepUnderwaterCaveSpawnRules(EntityType<DeepLatexSquidEntity> pEntityType, 
            net.minecraft.world.level.ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, 
            net.minecraft.util.RandomSource pRandom) {
        // Must be in water
        if (!pLevel.getFluidState(pPos).is(net.minecraft.tags.FluidTags.WATER)) {
            return false;
        }
        
        // Must be in a cave (below sea level and not exposed to sky)
        // Use a fixed sea level of 63 (default Minecraft sea level) if getSeaLevel() is not available
        int seaLevel = 63;
        if (pLevel instanceof net.minecraft.world.level.Level level) {
            seaLevel = level.getSeaLevel();
        }
        if (pPos.getY() >= seaLevel - 10) {
            return false; // Not deep enough
        }
        
        // Check if it's in a cave (surrounded by solid blocks, not exposed to sky)
        boolean isInCave = true;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos checkPos = pPos.offset(x, 0, z);
                // Check if there's a solid block above (cave ceiling)
                BlockPos abovePos = checkPos.above(5);
                if (pLevel.canSeeSky(abovePos)) {
                    isInCave = false;
                    break;
                }
            }
            if (!isInCave) break;
        }
        
        if (!isInCave) {
            return false;
        }
        
        // Check if there's enough water around (at least 2 blocks deep)
        int waterDepth = 0;
        for (int y = pPos.getY(); y <= pPos.getY() + 3; y++) {
            BlockPos waterCheck = new BlockPos(pPos.getX(), y, pPos.getZ());
            if (pLevel.getFluidState(waterCheck).is(net.minecraft.tags.FluidTags.WATER)) {
                waterDepth++;
            } else {
                break;
            }
        }
        
        if (waterDepth < 2) {
            return false;
        }
        
        // Check light level (should be dark in caves)
        int lightLevel = pLevel.getMaxLocalRawBrightness(pPos);
        if (lightLevel > 7) {
            return false; // Too bright for cave spawn
        }
        
        return true;
    }
}

