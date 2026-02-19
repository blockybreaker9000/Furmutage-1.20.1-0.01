package net.jerika.furmutage.mixins;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Previously applied fast water movement to Changed/Furmutage entities.
 * That behavior was removed; water movement is now vanilla and entities
 * in water receive Dolphin's Grace via ChangedEntitySwimEvents.
 */
@Mixin(LivingEntity.class)
public class LivingEntitySwimMixin {
}

