package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom behavior for grab entities:
 * - latex_shark_feral: When it successfully attacks something, it "grabs" the target by making it ride the shark
 *   after a short delay (GRAB_DELAY_TICKS).
 * - While grabbed, the target stays mounted on the entity.
 * - Once the entity reaches half of its maximum health, it ejects all passengers.
 *
 * This uses registry name checks so we don't need direct access to the Changed entity class.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexSharkGrabEvents {

    /** Delay in ticks before the grab is applied after a successful attack (300 = 15 seconds). */
    private static final int GRAB_DELAY_TICKS = 300;

    private static final ResourceLocation LATEX_SHARK_FERAL_ID =
            new ResourceLocation("changed", "latex_shark_feral");
    private static boolean isLatexSharkFeral(Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        var id = ForgeRegistries.ENTITY_TYPES.getKey(living.getType());
        return LATEX_SHARK_FERAL_ID.equals(id);
    }
    
    private static boolean isGrabEntity(Entity entity) {
        return isLatexSharkFeral(entity);
    }

    /** Shark -> (target to grab, ticks remaining until grab). */
    private static final Map<LivingEntity, PendingGrab> pendingGrabs = new HashMap<>();

    private static final class PendingGrab {
        final LivingEntity target;
        int ticksRemaining;

        PendingGrab(LivingEntity target, int ticksRemaining) {
            this.target = target;
            this.ticksRemaining = ticksRemaining;
        }
    }

    /**
     * When a grab entity (latex_shark_feral) successfully attacks a target,
     * schedule a grab after GRAB_DELAY_TICKS. The actual grab is applied in onSharkTick.
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        if (attacker == null || target.level().isClientSide) {
            return;
        }

        if (!isGrabEntity(attacker)) {
            return;
        }

        LivingEntity grabEntity = (LivingEntity) attacker;

        // Don't grab if entity is already carrying something or target is already riding
        if (grabEntity.isVehicle() || target.isPassenger()) {
            return;
        }

        // Schedule grab after delay (replaces any existing pending grab for this shark)
        pendingGrabs.put(grabEntity, new PendingGrab(target, GRAB_DELAY_TICKS));
    }

    /**
     * Each tick, if a grab entity (latex_shark_feral) is carrying a passenger
     * and its health drops to half or below, eject all passengers (release the grabbed entity).
     * Also processes pending grabs: after GRAB_DELAY_TICKS, applies the grab.
     */
    @SubscribeEvent
    public static void onSharkTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide) {
            return;
        }

        if (!isGrabEntity(entity)) {
            return;
        }

        LivingEntity grabEntity = entity;

        // Clean up pending grabs for sharks that are no longer valid
        pendingGrabs.entrySet().removeIf(e -> !e.getKey().isAlive() || !e.getValue().target.isAlive());

        // Process pending grab for this shark
        PendingGrab pending = pendingGrabs.get(grabEntity);
        if (pending != null) {
            if (!grabEntity.isAlive() || !pending.target.isAlive()
                    || grabEntity.isVehicle() || pending.target.isPassenger()) {
                pendingGrabs.remove(grabEntity);
            } else {
                pending.ticksRemaining--;
                if (pending.ticksRemaining <= 0) {
                    pendingGrabs.remove(grabEntity);
                    pending.target.startRiding(grabEntity, true);
                }
            }
        }

        if (!grabEntity.isVehicle()) {
            return; // Not carrying anyone
        }

        float halfHealth = grabEntity.getMaxHealth() * 0.5f;
        if (grabEntity.getHealth() <= halfHealth) {
            grabEntity.ejectPassengers();
        }
    }
}

