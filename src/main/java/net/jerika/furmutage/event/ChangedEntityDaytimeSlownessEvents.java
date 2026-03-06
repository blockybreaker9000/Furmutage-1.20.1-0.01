package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntityDaytimeSlownessEvents {

    private static final String CHANGED_NAMESPACE = "changed:";
    private static final String EXCLUDED_ROOMBA = "changed:roomba";
    private static final String EXCLUDED_EXOSKELETON = "changed:exoskeleton";
    private static final long NIGHT_START = 13000;
    private static final long NIGHT_END = 23000;
    // Block light threshold high enough to represent torch/lantern-level light in caves.
    private static final int TORCH_LIGHT_THRESHOLD = 10;
    private static final int CHECK_INTERVAL = 60;

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide()) {
            return;
        }
        if (!entity.isAlive()) {
            return;
        }
        if (entity.tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        String entityType = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        if (!entityType.startsWith(CHANGED_NAMESPACE)) {
            return;
        }
        if (EXCLUDED_ROOMBA.equals(entityType) || EXCLUDED_EXOSKELETON.equals(entityType)) {
            return;
        }

        long dayTime = entity.level().getLevelData().getDayTime() % 24000;
        boolean isDaytime = dayTime < NIGHT_START || dayTime >= NIGHT_END;

        if (isDaytime) {
            BlockPos pos = entity.blockPosition();
            // Sunlight: direct sky access.
            boolean inSunlight = entity.level().canSeeSky(pos.above());
            // Torch/cave light: strong block light near the entity.
            int blockLight = entity.level().getBrightness(LightLayer.BLOCK, pos);
            boolean inTorchLight = blockLight >= TORCH_LIGHT_THRESHOLD;

            // Only slow Changed entities during the day when they are actually in bright light
            // (sunlight on the surface or strong torch light in caves).
            if (inSunlight || inTorchLight) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 2, false, false, false));
            }
        } else {
            // Nighttime: give Changed entities Speed I; torch light has no slowing effect.
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1, false, false, false));
        }
    }
}
