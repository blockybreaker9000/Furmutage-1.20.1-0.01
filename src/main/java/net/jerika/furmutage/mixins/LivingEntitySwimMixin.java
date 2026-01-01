package net.jerika.furmutage.mixins;

import net.jerika.furmutage.event.ChangedEntitySwimEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to add fast swimming to Changed mod entities.
 * Intercepts travel() method to apply fast water movement.
 */
@Mixin(LivingEntity.class)
public class LivingEntitySwimMixin {
    
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void onTravel(Vec3 pTravelVector, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        // Check if this entity should have fast swimming
        if (ChangedEntitySwimEvents.shouldHaveFastSwimming(entity)) {
            // Apply fast swimming behavior (slower than ExoMutant)
            if (entity.isEffectiveAi() && entity.isInWater()) {
                // Reduced water movement speed - slower for better pathfinding
                entity.moveRelative(0.3f, pTravelVector);
                entity.move(MoverType.SELF, entity.getDeltaMovement());
                entity.setDeltaMovement(entity.getDeltaMovement().scale(0.9D)); // Slight drag
                ci.cancel(); // Cancel original travel() call
            }
        }
    }
}

