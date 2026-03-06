package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntityDaytimeSlownessEvents {

    private static final String CHANGED_NAMESPACE = "changed:";
    private static final String EXCLUDED_ROOMBA = "changed:roomba";
    private static final String EXCLUDED_EXOSKELETON = "changed:exoskeleton";
    private static final long NIGHT_START = 13000;
    private static final long NIGHT_END = 23000;
    private static final int TORCH_LIGHT_THRESHOLD = 10;
    private static final int CHECK_INTERVAL = 60;
    /** Speed applied when Changed mob is in light (instead of slowness potion). */
    private static final double SPEED_IN_LIGHT = 0.6D;
    /** Speed applied when it's night and the Changed mob is not in light. */
    private static final double SPEED_AT_NIGHT = 1.1D;

    /** Stores original MOVEMENT_SPEED base value per entity so we can restore when they leave light. */
    private static final Map<UUID, Double> storedSpeedWhenInLight = new ConcurrentHashMap<>();
    /** Stores original MOVEMENT_SPEED base value per entity so we can restore when it's no longer night. */
    private static final Map<UUID, Double> storedSpeedAtNight = new ConcurrentHashMap<>();

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

        AttributeInstance moveAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (moveAttr == null) {
            return;
        }

        long dayTime = entity.level().getLevelData().getDayTime() % 24000;
        boolean isDaytime = dayTime < NIGHT_START || dayTime >= NIGHT_END;
        boolean isNight = !isDaytime;

        BlockPos pos = entity.blockPosition();
        boolean inSunlight = entity.level().canSeeSky(pos.above());
        int blockLight = entity.level().getBrightness(LightLayer.BLOCK, pos);
        boolean inTorchLight = blockLight >= TORCH_LIGHT_THRESHOLD;

        boolean inLight = (isDaytime && (inSunlight || inTorchLight)) || (isNight && inTorchLight);

        if (inLight) {
            // If we had applied a night speed boost, restore the original before applying light slowdown
            Double originalNight = storedSpeedAtNight.remove(entity.getUUID());
            double baseBeforeLight = originalNight != null ? originalNight : moveAttr.getBaseValue();

            // Store original speed the first time we apply in-light speed, then set to SPEED_IN_LIGHT
            storedSpeedWhenInLight.putIfAbsent(entity.getUUID(), baseBeforeLight);
            moveAttr.setBaseValue(SPEED_IN_LIGHT);
        } else {
            // Not in light: if we previously slowed them in light, restore that original speed first
            Double originalInLight = storedSpeedWhenInLight.remove(entity.getUUID());
            if (originalInLight != null) {
                moveAttr.setBaseValue(originalInLight);
            }

            double currentBase = moveAttr.getBaseValue();

            if (isNight) {
                // At night (and not in light): boost speed, remembering original so we can restore later
                storedSpeedAtNight.putIfAbsent(entity.getUUID(), currentBase);
                moveAttr.setBaseValue(SPEED_AT_NIGHT);
            } else {
                // Daytime and not in light: if we had a night boost stored, restore it
                Double originalNight = storedSpeedAtNight.remove(entity.getUUID());
                if (originalNight != null) {
                    moveAttr.setBaseValue(originalNight);
                }
            }
        }

        if (isDaytime) {
            // Daytime in light is handled above (speed 0.7). No potion.
        } else {
            // Nighttime: keep Resistance I and Jump Boost II
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 300, 0, false, false, false));
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        storedSpeedWhenInLight.remove(event.getEntity().getUUID());
        storedSpeedAtNight.remove(event.getEntity().getUUID());
    }
}
