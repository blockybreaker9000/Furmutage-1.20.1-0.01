package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Makes Changed entities crouch and move slower when stalking a player target
 * that cannot currently see them (off-screen).
 *
 * - Applies to any entity whose registry namespace is "changed" and is a Mob.
 * - If the entity has a player target AND is NOT on that player's screen:
 *     * Sets pose to CROUCHING
 *     * Reduces movement speed to a fraction of its original base value
 * - When the player can see them again (on-screen) or there is no valid target:
 *     * Restores original movement speed
 *     * Sets pose back to STANDING
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedStalkerSneakEvents {

    // Keys for storing original speed / state on the entity
    private static final String TAG_ORIG_SPEED = "furmutage_changed_orig_speed";
    private static final String TAG_SNEAKING = "furmutage_changed_sneaking";
    // Last tick when this entity was \"seen\" (had line of sight) by its target player
    private static final String TAG_LAST_SEEN_TICK = "furmutage_changed_last_seen_tick";
    private static final double MIN_FOLLOW_RANGE = 30.0D;

    /**
     * Ensure Changed entities can see/track their targets from at least 30 blocks away
     * by bumping their FOLLOW_RANGE attribute when they join the world.
     */
    @SubscribeEvent
    public static void onChangedJoinWorld(EntityJoinLevelEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof LivingEntity living)) {
            return;
        }

        if (!isChangedEntity(living)) {
            return;
        }

        AttributeInstance followAttr = living.getAttribute(Attributes.FOLLOW_RANGE);
        if (followAttr == null) {
            return;
        }

        double current = followAttr.getBaseValue();
        if (current < MIN_FOLLOW_RANGE) {
            followAttr.setBaseValue(MIN_FOLLOW_RANGE);
        }
    }

    @SubscribeEvent
    public static void onChangedTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide) {
            return;
        }

        if (!isChangedEntity(entity)) {
            return;
        }

        // Only care about mobs that can have an attack target
        if (!(entity instanceof Mob mob)) {
            return;
        }

        AttributeInstance moveAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (moveAttr == null) {
            return;
        }

        CompoundTag data = entity.getPersistentData();
        boolean wasSneaking = data.getBoolean(TAG_SNEAKING);

        // Only care about living targets (usually players)
        LivingEntity target = mob.getTarget();
        boolean shouldSneak = false;

        if (target instanceof Player player) {
            int currentTick = entity.tickCount;
            int lastSeenTick = data.getInt(TAG_LAST_SEEN_TICK);

            boolean seenNow = isEntityOnScreenForPlayer(player, entity);

            // Update last seen time whenever the player can currently see this entity
            if (seenNow) {
                data.putInt(TAG_LAST_SEEN_TICK, currentTick);
            }

            // Allow crouch AI to re-enable only if 30 seconds (600 ticks) have passed
            // since the last time this entity was seen by its target.
            boolean enoughTimeSinceSeen = (lastSeenTick == 0) || (currentTick - lastSeenTick >= 600);

            // We want to sneak only when:
            // - The player cannot currently see the entity
            // - AND it has been at least 30 seconds since the last time it was seen
            shouldSneak = !seenNow && enoughTimeSinceSeen;
        }

        // Start sneaking: slow down and crouch
        if (shouldSneak && !wasSneaking) {
            if (!data.contains(TAG_ORIG_SPEED)) {
                data.putDouble(TAG_ORIG_SPEED, moveAttr.getBaseValue());
            }

            double base = data.getDouble(TAG_ORIG_SPEED);
            // Base sneaking speed: 50% of original
            moveAttr.setBaseValue(base * 0.50D);
            entity.setPose(Pose.CROUCHING);

            data.putBoolean(TAG_SNEAKING, true);
        }
        // Stop sneaking: restore speed and pose
        else if (!shouldSneak && wasSneaking) {
            if (data.contains(TAG_ORIG_SPEED)) {
                moveAttr.setBaseValue(data.getDouble(TAG_ORIG_SPEED));
            }
            entity.setPose(Pose.STANDING);
            data.putBoolean(TAG_SNEAKING, false);

        }
    }

    /**
     * Returns true if the entity's registry namespace is "changed".
     */
    private static boolean isChangedEntity(Entity entity) {
        var type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return type != null && "changed".equals(type.getNamespace());
    }

    /**
     * Checks if the player is actually LOOKING toward the entity within a
     * reasonable cone and has line of sight.
     *
     * This intentionally ignores third-person camera visibility: it only
     * cares about the player's look direction, not what the camera can see.
     */
    private static boolean isEntityOnScreenForPlayer(Player player, LivingEntity entity) {
        // Player look direction
        Vec3 look = player.getViewVector(1.0F).normalize();

        // Vector from player eyes to entity
        Vec3 playerEyePos = player.getEyePosition(1.0F);
        Vec3 toEntity = new Vec3(
                entity.getX() - playerEyePos.x,
                entity.getEyeY() - playerEyePos.y,
                entity.getZ() - playerEyePos.z
        ).normalize();

        double dot = look.dot(toEntity);
        // cos(60°) ~ 0.5; require the entity to be roughly within a 60-degree cone
        return dot > 0.5D && player.hasLineOfSight(entity);
    }
}

