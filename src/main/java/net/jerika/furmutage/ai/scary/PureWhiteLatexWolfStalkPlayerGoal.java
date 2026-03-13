package net.jerika.furmutage.ai.scary;

import net.jerika.furmutage.sound.ModSounds;
import net.jerika.furmutage.config.TransfurTeamHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumSet;
import java.util.List;

/**
 * Goal for pure white latex wolves that were marked as stalkers: sneak around,
 * watch the player, maintain a fixed distance.
 * - If player is &gt; 30 blocks away and looks at the wolf: rapid crouch/uncrouch + punch.
 * - If player is 10–30 blocks away and looks: walk away (crouched), no animation.
 * - If player gets within 10 blocks: stop crouching, run away and hide.
 * After ~3 seconds of running away/hiding, stalker AI deactivates and the wolf acts normal.
 * Does not set attack target; attacking is provocation-only (handled elsewhere).
 */
public class PureWhiteLatexWolfStalkPlayerGoal extends Goal {

    private static final String TAG_STALKER_WOLF = "furmutage_stalker_wolf";
    /** Ticks spent in run-away (player within 10 blocks); after HIDE_DURATION_TICKS we deactivate stalker. */
    private static final String TAG_STALKER_HIDE_TICKS = "furmutage_stalker_hide_ticks";
    /** Cooldown for playing latex_hello during spooked/walk-away. */
    private static final String TAG_LAST_STALKER_HELLO = "furmutage_stalker_last_hello_tick";
    /** ~3 seconds of run-away/hide before stalker AI turns off and wolf acts normal. */
    private static final int HIDE_DURATION_TICKS = 60;
    /** Cooldown between latex_hello plays during spooked or walk-away (ticks). */
    private static final int LATEX_HELLO_COOLDOWN_TICKS = 55;
    /** Distance (blocks) the wolf tries to maintain from the player. */
    private static final double STALK_DISTANCE = 15.0D;
    /** If closer than this, back away; if farther, move in. */
    private static final double DISTANCE_TOLERANCE = 2.0D;
    private static final double MIN_DISTANCE = STALK_DISTANCE - DISTANCE_TOLERANCE;
    private static final double MAX_DISTANCE = 22.0D;
    private static final double SNEAK_SPEED = 0.35D;
    /** Within this distance the wolf stands and runs away instead of crouch/walk. */
    private static final double RUN_AWAY_DISTANCE = 2.0D;
    /** Spooked (crouch/uncrouch + punch) only when player is MORE than this many blocks away.
     * Within 30 blocks we never do spooked—we prioritize walking/running away instead. */
    private static final double SPOOKED_BEHAVIOR_MIN_DISTANCE = 30.0D;
    /** Ticks between crouch/uncrouch toggles when player is looking. */
    private static final int CROUCH_TOGGLE_INTERVAL = 5;
    /** Cone: player look dot with to-entity vector must be above this (cos ~45°). */
    private static final double LOOK_AT_DOT = 0.7D;
    /** Speed when walking away while crouched (player 10–30 blocks, looking). */
    private static final double WALK_AWAY_SPEED = 0.5D;

    @SuppressWarnings("unchecked")
    private static final RegistryObject<net.minecraft.sounds.SoundEvent>[] LATEX_HELLO_SOUNDS = new RegistryObject[] {
        ModSounds.LATEX_HELLO_1, ModSounds.LATEX_HELLO_2, ModSounds.LATEX_HELLO_3, ModSounds.LATEX_HELLO_4,
        ModSounds.LATEX_HELLO_5, ModSounds.LATEX_HELLO_6, ModSounds.LATEX_HELLO_7, ModSounds.LATEX_HELLO_8,
        ModSounds.LATEX_HELLO_9, ModSounds.LATEX_HELLO_10, ModSounds.LATEX_HELLO_11, ModSounds.LATEX_HELLO_12,
        ModSounds.LATEX_HELLO_13, ModSounds.LATEX_HELLO_14, ModSounds.LATEX_HELLO_15, ModSounds.LATEX_HELLO_16,
        ModSounds.LATEX_HELLO_17, ModSounds.LATEX_HELLO_18, ModSounds.LATEX_HELLO_19, ModSounds.LATEX_HELLO_20
    };

    private final PathfinderMob mob;
    private Player watchedPlayer;
    private int rePathTicks;
    private int crouchToggleTicks;
    private boolean wasCrouchedWhenSpooked;

    public PureWhiteLatexWolfStalkPlayerGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public static boolean isStalkerWolf(PathfinderMob mob) {
        return mob.getPersistentData().getBoolean(TAG_STALKER_WOLF);
    }

    /** True if the player is looking roughly at the mob (cone + line of sight). */
    private static boolean isPlayerLookingAt(Player player, PathfinderMob mob) {
        Vec3 look = player.getViewVector(1.0F).normalize();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 toMob = new Vec3(
                mob.getX() - eyePos.x,
                mob.getEyeY() - eyePos.y,
                mob.getZ() - eyePos.z
        ).normalize();
        return look.dot(toMob) >= LOOK_AT_DOT && player.hasLineOfSight(mob);
    }

    /** Play a random latex_hello if cooldown has passed. Call during spooked or walk-away. */
    private void tryPlayLatexHello() {
        var data = mob.getPersistentData();
        long now = mob.level().getGameTime();
        if (now - data.getLong(TAG_LAST_STALKER_HELLO) >= LATEX_HELLO_COOLDOWN_TICKS) {
            var sound = LATEX_HELLO_SOUNDS[mob.getRandom().nextInt(LATEX_HELLO_SOUNDS.length)].get();
            if (sound != null) {
                mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), sound, SoundSource.HOSTILE, 0.8F, 0.9F + mob.getRandom().nextFloat() * 0.2F);
                data.putLong(TAG_LAST_STALKER_HELLO, now);
            }
        }
    }

    @Override
    public boolean canUse() {
        if (!isStalkerWolf(mob)) {
            return false;
        }
        // Only stalk when we don't have an attack target (provocation sets target elsewhere)
        if (mob.getTarget() != null) {
            return false;
        }
        List<Player> players = mob.level().getEntitiesOfClass(
                Player.class,
                mob.getBoundingBox().inflate(MAX_DISTANCE, 8.0D, MAX_DISTANCE),
                p -> p != null && p.isAlive() && !p.isSpectator() && !TransfurTeamHelper.isPlayerTransfurred(p)
        );
        if (players.isEmpty()) {
            return false;
        }
        Player nearest = null;
        double nearestSq = Double.MAX_VALUE;
        for (Player p : players) {
            double dSq = mob.distanceToSqr(p);
            if (dSq < nearestSq && dSq <= MAX_DISTANCE * MAX_DISTANCE) {
                nearestSq = dSq;
                nearest = p;
            }
        }
        watchedPlayer = nearest;
        return watchedPlayer != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (watchedPlayer == null || !watchedPlayer.isAlive()) {
            return false;
        }
        if (TransfurTeamHelper.isPlayerTransfurred(watchedPlayer)) {
            return false;
        }
        if (mob.getTarget() != null) {
            return false;
        }
        double dSq = mob.distanceToSqr(watchedPlayer);
        return dSq <= (MAX_DISTANCE + 2) * (MAX_DISTANCE + 2);
    }

    @Override
    public void start() {
        rePathTicks = 0;
        crouchToggleTicks = 0;
        wasCrouchedWhenSpooked = true;
        mob.setPose(Pose.CROUCHING);
    }

    @Override
    public void stop() {
        watchedPlayer = null;
        mob.getNavigation().stop();
        mob.setPose(Pose.STANDING);
    }

    @Override
    public void tick() {
        if (watchedPlayer == null) {
            return;
        }
        var data = mob.getPersistentData();
        // Reset hide timer when not in run-away so only consecutive run-away time counts
        boolean inRunAwayZone = mob.distanceToSqr(watchedPlayer) <= RUN_AWAY_DISTANCE * RUN_AWAY_DISTANCE;
        if (!inRunAwayZone) {
            data.putInt(TAG_STALKER_HIDE_TICKS, 0);
        }

        mob.getLookControl().setLookAt(watchedPlayer, 30.0F, 30.0F);

        boolean playerLookingAtMe = isPlayerLookingAt(watchedPlayer, mob);
        double dSq = mob.distanceToSqr(watchedPlayer);
        double minSq = MIN_DISTANCE * MIN_DISTANCE;
        double maxPreferredSq = (STALK_DISTANCE + DISTANCE_TOLERANCE) * (STALK_DISTANCE + DISTANCE_TOLERANCE);
        double runAwaySq = RUN_AWAY_DISTANCE * RUN_AWAY_DISTANCE;
        double spookedMinSq = SPOOKED_BEHAVIOR_MIN_DISTANCE * SPOOKED_BEHAVIOR_MIN_DISTANCE;

        // Player within 10 blocks: stop crouching, run away and hide. After ~3 sec hide, deactivate stalker AI.
        if (dSq <= runAwaySq) {
            int hideTicks = data.getInt(TAG_STALKER_HIDE_TICKS) + 1;
            data.putInt(TAG_STALKER_HIDE_TICKS, hideTicks);
            if (hideTicks >= HIDE_DURATION_TICKS) {
                data.putBoolean(TAG_STALKER_WOLF, false);
                mob.setPose(Pose.STANDING);
                mob.getNavigation().stop();
                return;
            }
            mob.setPose(Pose.STANDING);
            rePathTicks--;
            if (rePathTicks <= 0) {
                rePathTicks = 15 + mob.getRandom().nextInt(15);
                Vec3 away = mob.position().subtract(watchedPlayer.position()).normalize().scale(16.0D);
                Vec3 target = mob.position().add(away);
                double runSpeed = mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                mob.getNavigation().moveTo(target.x, target.y, target.z, runSpeed > 0 ? runSpeed : 0.7D);
            }
            return;
        }

        // Player 10–30 blocks away and looking at us: walk away (crouched). No punch/uncrouch—prioritize fleeing.
        if (playerLookingAtMe && dSq <= spookedMinSq) {
            tryPlayLatexHello();
            mob.setPose(Pose.CROUCHING);
            rePathTicks--;
            if (rePathTicks <= 0) {
                rePathTicks = 20 + mob.getRandom().nextInt(20);
                Vec3 away = mob.position().subtract(watchedPlayer.position()).normalize().scale(12.0D);
                Vec3 target = mob.position().add(away);
                mob.getNavigation().moveTo(target.x, target.y, target.z, WALK_AWAY_SPEED);
            }
            return;
        }

        // Player MORE than 30 blocks away and looking: spooked (crouch/uncrouch + punch). Only when far.
        if (playerLookingAtMe && dSq > spookedMinSq) {
            tryPlayLatexHello();
            mob.getNavigation().stop();
            crouchToggleTicks++;
            if (crouchToggleTicks >= CROUCH_TOGGLE_INTERVAL) {
                crouchToggleTicks = 0;
                wasCrouchedWhenSpooked = !wasCrouchedWhenSpooked;
                mob.setPose(wasCrouchedWhenSpooked ? Pose.CROUCHING : Pose.STANDING);
                mob.swing(InteractionHand.MAIN_HAND);
            }
            return;
        }

        // Normal stalking: stay at STALK_DISTANCE (within tolerance)
        mob.setPose(Pose.CROUCHING);
        rePathTicks--;
        if (rePathTicks <= 0) {
            rePathTicks = 20 + mob.getRandom().nextInt(20);
            if (dSq > maxPreferredSq) {
                Path path = mob.getNavigation().createPath(watchedPlayer, 0);
                if (path != null && path.canReach()) {
                    mob.getNavigation().moveTo(path, SNEAK_SPEED);
                } else {
                    mob.getNavigation().moveTo(watchedPlayer, SNEAK_SPEED);
                }
            } else if (dSq < minSq) {
                Vec3 away = mob.position().subtract(watchedPlayer.position()).normalize().scale(3.0D);
                Vec3 target = mob.position().add(away);
                mob.getNavigation().moveTo(target.x, target.y, target.z, SNEAK_SPEED);
            } else {
                mob.getNavigation().stop();
            }
        }
    }
}
