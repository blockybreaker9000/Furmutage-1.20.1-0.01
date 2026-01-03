package net.jerika.furmutage.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to give Changed entities x-ray vision through up to 5 blocks.
 * Modifies the hasLineOfSight method to allow seeing through blocks.
 */
@Mixin(Sensing.class)
public class EntitySensingXRayMixin {
    @Shadow
    @Final
    private Mob mob;
    
    private static final int XRAY_BLOCK_LIMIT = 5; // Can see through up to 5 blocks
    
    @Inject(method = "hasLineOfSight", at = @At("HEAD"), cancellable = true)
    private void onHasLineOfSight(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        // Only process if the target is a LivingEntity
        if (!(pEntity instanceof LivingEntity target)) {
            return;
        }
        
        // Check if this is a Changed entity (excluding roomba)
        LivingEntity livingEntity = this.mob;
        
        // Check if entity should have x-ray vision
        if (shouldHaveXRayVision(livingEntity)) {
            // Use custom x-ray vision check
            boolean canSee = hasXRayVision(livingEntity, target, XRAY_BLOCK_LIMIT);
            if (canSee) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }
    
    /**
     * Check if an entity should have x-ray vision (Changed entity, not roomba)
     */
    private boolean shouldHaveXRayVision(LivingEntity entity) {
        net.minecraft.resources.ResourceLocation entityTypeId = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (entityTypeId == null) {
            return false;
        }
        
        String entityId = entityTypeId.toString();
        return entityId.startsWith("changed:") && !entityId.equals("changed:roomba");
    }
    
    /**
     * Check if the mob can see the target through up to the specified number of blocks.
     * Counts solid blocks between the mob and target, and allows vision if there are
     * maxBlocks or fewer solid blocks.
     */
    private boolean hasXRayVision(LivingEntity mob, LivingEntity target, int maxBlocks) {
        Vec3 mobEyePos = mob.getEyePosition();
        Vec3 targetEyePos = target.getEyePosition();
        
        // Calculate direction vector
        Vec3 direction = targetEyePos.subtract(mobEyePos);
        double distance = direction.length();
        
        if (distance > 60.0D) { // Respect follow range
            return false;
        }
        
        // Normalize direction
        direction = direction.normalize();
        
        // Sample points along the line of sight
        int samples = Math.max((int) (distance * 2), 10); // Sample every 0.5 blocks, minimum 10 samples
        int solidBlockCount = 0;
        
        Level level = mob.level();
        
        for (int i = 1; i < samples; i++) {
            double t = (double) i / samples;
            Vec3 samplePos = mobEyePos.add(direction.scale(distance * t));
            BlockPos blockPos = BlockPos.containing(samplePos);
            
            BlockState state = level.getBlockState(blockPos);
            
            // Check if this block is solid (blocks vision)
            if (!state.isAir() && !state.getCollisionShape(level, blockPos).isEmpty()) {
                solidBlockCount++;
                
                // If we've exceeded the limit, can't see through
                if (solidBlockCount > maxBlocks) {
                    return false;
                }
            }
        }
        
        // Can see through if solid block count is within limit
        return solidBlockCount <= maxBlocks;
    }
}

