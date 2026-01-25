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

/**
 * Custom behavior for grab entities:
 * - latex_shark_feral: When it successfully attacks something, it "grabs" the target by making it ride the shark.
 * - DeepSlateLatexSquidDog: Same grab behavior as the feral shark.
 * - While grabbed, the target stays mounted on the entity.
 * - Once the entity reaches half of its maximum health, it ejects all passengers.
 *
 * This uses registry name checks so we don't need direct access to the Changed entity class.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexSharkGrabEvents {

    private static final ResourceLocation LATEX_SHARK_FERAL_ID =
            new ResourceLocation("changed", "latex_shark_feral");
    private static final ResourceLocation DEEPSLATE_LATEX_SQUID_DOG_ID =
            new ResourceLocation("furmutage", "deepslate_latex_squid_dog");

    private static boolean isLatexSharkFeral(Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        var id = ForgeRegistries.ENTITY_TYPES.getKey(living.getType());
        return LATEX_SHARK_FERAL_ID.equals(id);
    }
    
    private static boolean isDeepSlateLatexSquidDog(Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        var id = ForgeRegistries.ENTITY_TYPES.getKey(living.getType());
        return DEEPSLATE_LATEX_SQUID_DOG_ID.equals(id);
    }
    
    private static boolean isGrabEntity(Entity entity) {
        return isLatexSharkFeral(entity) || isDeepSlateLatexSquidDog(entity);
    }

    /**
     * When a grab entity (latex_shark_feral or DeepSlateLatexSquidDog) successfully attacks a target,
     * make the target ride the entity (simulating a grab / bite). The entity keeps hitting the grabbed target as normal.
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

        // Start riding: target becomes a passenger of the entity
        // 'true' forces the mount even if target is already riding something else
        target.startRiding(grabEntity, true);
    }

    /**
     * Each tick, if a grab entity (latex_shark_feral or DeepSlateLatexSquidDog) is carrying a passenger
     * and its health drops to half or below, eject all passengers (release the grabbed entity).
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

        if (!grabEntity.isVehicle()) {
            return; // Not carrying anyone
        }

        float halfHealth = grabEntity.getMaxHealth() * 0.5f;
        if (grabEntity.getHealth() <= halfHealth) {
            grabEntity.ejectPassengers();
        }
    }
}

