package net.jerika.furmutage.ai.watching_you;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

/**
 * Goal that makes Changed entities occasionally stare at players from far away.
 * Very small chance to activate so it feels rare and creepy.
 */
public class ChangedDistantStareGoal extends Goal {
    private final PathfinderMob mob;
    private Player targetPlayer;
    private int stareTime;
    private static final double STARE_RANGE = 100.0; // Stare at players up to 100 blocks away
    private static final double MIN_STARE_RANGE = 32.0; // Minimum distance to start staring
    private static final int MIN_STARE_DURATION = 400; // Minimum stare duration
    private static final int MAX_STARE_DURATION = 2000; // Maximum stare duration
    private static final double ACTIVATION_CHANCE = 0.0002; // 0.02% chance per tick when conditions are met

    public ChangedDistantStareGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Very small chance to activate (0.2% per tick)
        if (this.mob.getRandom().nextDouble() >= ACTIVATION_CHANCE) {
            return false;
        }

        // Find nearby players
        List<Player> players = this.mob.level().getEntitiesOfClass(
                Player.class,
                this.mob.getBoundingBox().inflate(STARE_RANGE, STARE_RANGE / 2, STARE_RANGE),
                player -> player != null &&
                        player.isAlive() &&
                        !player.isSpectator() &&
                        this.mob.distanceTo(player) >= MIN_STARE_RANGE &&
                        this.mob.distanceTo(player) <= STARE_RANGE &&
                        this.mob.getSensing().hasLineOfSight(player)
        );

        if (players.isEmpty()) {
            return false;
        }

        // Pick a random player to stare at
        this.targetPlayer = players.get(this.mob.getRandom().nextInt(players.size()));
        this.stareTime = MIN_STARE_DURATION + this.mob.getRandom().nextInt(MAX_STARE_DURATION - MIN_STARE_DURATION);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetPlayer == null || !this.targetPlayer.isAlive() || this.targetPlayer.isSpectator()) {
            return false;
        }

        double distance = this.mob.distanceTo(this.targetPlayer);
        if (distance < MIN_STARE_RANGE || distance > STARE_RANGE) {
            return false;
        }

        if (!this.mob.getSensing().hasLineOfSight(this.targetPlayer)) {
            return false;
        }

        return this.stareTime > 0;
    }

    @Override
    public void start() {
        this.stareTime = MIN_STARE_DURATION + this.mob.getRandom().nextInt(MAX_STARE_DURATION - MIN_STARE_DURATION);
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
        this.stareTime = 0;
    }

    @Override
    public void tick() {
        if (this.targetPlayer != null) {
            // Look directly at the player
            this.mob.getLookControl().setLookAt(
                    this.targetPlayer.getX(),
                    this.targetPlayer.getEyeY(),
                    this.targetPlayer.getZ(),
                    30.0F,
                    30.0F
            );
            this.stareTime--;
        }
    }
}


