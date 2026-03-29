package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.config.ModCommonConfig;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Makes Changed entities crouch and move slower when stalking a player target
 * that cannot currently see them (off-screen).
 *
 * - Applies to any entity whose registry namespace is "changed" and is a Mob.
 * - Also applies to {@code furmutage:loose_behemoth_hand} and {@code furmutage:loose_squid_dog_limb} at night only:
 *     same stalk rules, crouch pose, and movement speed base set to 0.6.
 * - If the entity has a player target AND is NOT on that player's screen:
 *     * Sets pose to CROUCHING
 *     * For Changed: reduces movement speed to a fraction of its original base value
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
    // Whether this stalker AI has been activated at least once for this entity
    private static final String TAG_AI_ACTIVATED = "furmutage_changed_ai_activated";
    // Player persistent data key for jumpscare cooldown (game time when last played)
    private static final String PLAYER_TAG_LAST_JUMPSCARE = "furmutage_last_jumpscare_tick";
    // Cooldown: 5 minutes = 6000 ticks keeps it from being annoying and ruining the scare
    private static final long JUMPSCARE_COOLDOWN_TICKS = 6000;
    // Max distance (blocks) for jumpscare sound - only plays when entity is close for an extra fright
    private static final double JUMPSCARE_MAX_DISTANCE = 6.0D;
    // Latex hello: play when crouched following - 1/5000 chance per tick, 4000 tick cooldown
    private static final String TAG_LAST_LATEX_HELLO_TICK = "furmutage_last_latex_hello_tick";
    private static final long LATEX_HELLO_COOLDOWN_TICKS = 4000L;
    private static final double LATEX_HELLO_CHANCE = 1.0 / 5000.0;
    @SuppressWarnings("unchecked")
    private static final RegistryObject<net.minecraft.sounds.SoundEvent>[] LATEX_HELLO_SOUNDS = new RegistryObject[] {
        ModSounds.LATEX_HELLO_1,
        ModSounds.LATEX_HELLO_2,
        ModSounds.LATEX_HELLO_3,
        ModSounds.LATEX_HELLO_4,
        ModSounds.LATEX_HELLO_5,
        ModSounds.LATEX_HELLO_6,
        ModSounds.LATEX_HELLO_7,
        ModSounds.LATEX_HELLO_8,
        ModSounds.LATEX_HELLO_9,
        ModSounds.LATEX_HELLO_10,
        ModSounds.LATEX_HELLO_11,
        ModSounds.LATEX_HELLO_12,
        ModSounds.LATEX_HELLO_13,
        ModSounds.LATEX_HELLO_14,
        ModSounds.LATEX_HELLO_15,
        ModSounds.LATEX_HELLO_16,
        ModSounds.LATEX_HELLO_17,
        ModSounds.LATEX_HELLO_18,
        ModSounds.LATEX_HELLO_19,
        ModSounds.LATEX_HELLO_20
    };
    private static final double MIN_FOLLOW_RANGE = 16.0D;

    private static final String LOOSE_BEHEMOTH_HAND_ID = "furmutage:loose_behemoth_hand";
    private static final String LOOSE_SQUID_DOG_LIMB_ID = "furmutage:loose_squid_dog_limb";
    /** Movement speed base while crouch-stalking toward the player (night only for loose Furmutage mobs). */
    private static final double LOOSE_STALKER_NIGHT_SPEED = 0.6D;
    private static final long NIGHT_START = 13000L;
    private static final long NIGHT_END = 23000L;

    /**
     * When the common-config toggle is on: ensure Changed entities have at least this
     * {@code FOLLOW_RANGE} (blocks) when they join the world if their base was lower.
     */
    @SubscribeEvent
    public static void onChangedJoinWorld(EntityJoinLevelEvent event) {
        if (!ModCommonConfig.ENABLE_CHANGED_LONG_RANGE_TARGETING_AND_SWIM_FOLLOW_RANGE.get()) {
            return;
        }

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

        boolean isChanged = isChangedEntity(entity);
        boolean isLooseStalker = isLooseStalkingMob(entity);
        if (!isChanged && !isLooseStalker) {
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

        if (isLooseStalker) {
            shouldSneak = shouldSneak && isNight(entity.level());
        }

        // When crouched and following: rarely play latex_hello (1 of 20 variants)
        if (isChanged && shouldSneak && wasSneaking && target != null) {
            long now = entity.level().getGameTime();
            long lastHello = data.getLong(TAG_LAST_LATEX_HELLO_TICK);
            if (now - lastHello >= LATEX_HELLO_COOLDOWN_TICKS && entity.getRandom().nextDouble() < LATEX_HELLO_CHANCE) {
                var sound = LATEX_HELLO_SOUNDS[entity.getRandom().nextInt(LATEX_HELLO_SOUNDS.length)].get();
                if (sound != null) {
                    // Subtler latex hello
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.HOSTILE, 0.4F, 0.9F + entity.getRandom().nextFloat() * 0.2F);
                    data.putLong(TAG_LAST_LATEX_HELLO_TICK, now);
                }
            }
        }

        // Start sneaking: slow down and crouch
        if (shouldSneak && !wasSneaking) {
            if (!data.contains(TAG_ORIG_SPEED)) {
                data.putDouble(TAG_ORIG_SPEED, moveAttr.getBaseValue());
            }

            if (isLooseStalker) {
                moveAttr.setBaseValue(LOOSE_STALKER_NIGHT_SPEED);
            } else {
                double base = data.getDouble(TAG_ORIG_SPEED);
                // Base sneaking speed: 50% of original
                moveAttr.setBaseValue(base * 0.50D);
            }
            entity.setPose(Pose.CROUCHING);

            data.putBoolean(TAG_SNEAKING, true);
            // Mark that this entity has had its stalker AI activated at least once
            data.putBoolean(TAG_AI_ACTIVATED, true);
        }
        // Stop sneaking: restore speed and pose
        else if (!shouldSneak && wasSneaking) {
            if (data.contains(TAG_ORIG_SPEED)) {
                moveAttr.setBaseValue(data.getDouble(TAG_ORIG_SPEED));
            }
            entity.setPose(Pose.STANDING);
            data.putBoolean(TAG_SNEAKING, false);

            // Play jumpscare sound only for the target player that activated the AI, once per 5 minutes, and only if within 6 blocks
            LivingEntity currentTarget = mob.getTarget();
            if (!isLooseStalker
                    && currentTarget instanceof ServerPlayer serverPlayer
                    && data.getBoolean(TAG_AI_ACTIVATED)) {
                if (entity.distanceTo(serverPlayer) <= JUMPSCARE_MAX_DISTANCE) {
                    long now = entity.level().getGameTime();
                    long lastPlayed = serverPlayer.getPersistentData().getLong(PLAYER_TAG_LAST_JUMPSCARE);
                    if (now - lastPlayed >= JUMPSCARE_COOLDOWN_TICKS) {
                        var jumpscares = new net.minecraft.sounds.SoundEvent[] {
                            ModSounds.LATEX_JUMPSCARE.get(),
                            ModSounds.LATEX_JUMPSCARE_2.get(),
                            ModSounds.LATEX_JUMPSCARE_3.get(),
                            ModSounds.LATEX_JUMPSCARE_4.get()
                        };
                        var sound = jumpscares[entity.level().getRandom().nextInt(jumpscares.length)];
                        serverPlayer.playNotifySound(sound, SoundSource.HOSTILE, 1.0F, 1.0F);
                        serverPlayer.getPersistentData().putLong(PLAYER_TAG_LAST_JUMPSCARE, now);
                    }
                }
            }
        }
    }

    /**
     * Returns true if the entity's registry namespace is "changed".
     */
    private static boolean isChangedEntity(Entity entity) {
        var type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return type != null && "changed".equals(type.getNamespace());
    }

    private static boolean isLooseStalkingMob(Entity entity) {
        var key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (key == null) {
            return false;
        }
        String id = key.toString();
        return LOOSE_BEHEMOTH_HAND_ID.equals(id) || LOOSE_SQUID_DOG_LIMB_ID.equals(id);
    }

    private static boolean isNight(Level level) {
        long dayTime = level.getDayTime() % 24000L;
        return dayTime >= NIGHT_START && dayTime < NIGHT_END;
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

