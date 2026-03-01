package net.jerika.furmutage.mixins;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents food-eating particles when consuming open canned food items.
 */
@Mixin(LivingEntity.class)
public class LivingEntityEatingParticlesMixin {

    @Inject(method = "makeItemParticles", at = @At("HEAD"), cancellable = true)
    private void furmutage$noParticlesForOpenCans(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        ItemStack useItem = self.getUseItem();
        if (useItem.isEmpty()) return;
        if (useItem.is(ModItems.OPEN_CANNED_CAT_FOOD.get())
                || useItem.is(ModItems.OPEN_CANNED_DOG_FOOD.get())
                || useItem.is(ModItems.OPEN_CANNED_BEANS.get())
                || useItem.is(ModItems.OPEN_CANNED_HAM.get())
                || useItem.is(ModItems.OPEN_CANNED_SOUP.get())) {
            ci.cancel();
        }
    }
}
