package net.jerika.furmutage.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to enable ladder climbing for Changed entities.
 * Overrides onClimbable() to return true when touching ladders or vines.
 */
@Mixin(LivingEntity.class)
public class LivingEntityLadderClimbMixin {
    
    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    private void onOnClimbable(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        // Check if this is a Changed entity (excluding roomba)
        if (shouldHaveLadderClimbing(entity)) {
            // Check if entity is touching a ladder or vine
            BlockPos pos = entity.blockPosition();
            BlockState state = entity.level().getBlockState(pos);
            Block block = state.getBlock();
            
            // Check if standing on or touching a ladder/vine
            if (block instanceof LadderBlock || block instanceof VineBlock) {
                cir.setReturnValue(true);
                cir.cancel();
                return;
            }
            
            // Also check the block the entity is in (for ladders on walls)
            BlockPos feetPos = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());
            BlockState feetState = entity.level().getBlockState(feetPos);
            Block feetBlock = feetState.getBlock();
            
            if (feetBlock instanceof LadderBlock || feetBlock instanceof VineBlock) {
                cir.setReturnValue(true);
                cir.cancel();
                return;
            }
            
            // Check blocks around the entity (for ladders on adjacent walls)
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && z == 0) continue;
                    BlockPos checkPos = feetPos.offset(x, 0, z);
                    BlockState checkState = entity.level().getBlockState(checkPos);
                    Block checkBlock = checkState.getBlock();
                    
                    if (checkBlock instanceof LadderBlock || checkBlock instanceof VineBlock) {
                        // Entity is near a ladder/vine, allow climbing
                        cir.setReturnValue(true);
                        cir.cancel();
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * Check if an entity should have ladder climbing (Changed entity, not roomba)
     */
    private boolean shouldHaveLadderClimbing(LivingEntity entity) {
        ResourceLocation entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (entityTypeId == null) {
            return false;
        }
        
        String entityId = entityTypeId.toString();
        return entityId.startsWith("changed:") && !entityId.equals("changed:roomba");
    }
}

