package net.jerika.furmutage.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.jerika.furmutage.ai.tsc_drone_ai.DroneFlyingRandomStrollGoal;
import net.jerika.furmutage.ai.tsc_drone_ai.DroneGrenadeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Boss version of the TSC drone:
 * - Uses same behavior as TSCDroneEntity but with much higher stats.
 * - Periodically summons additional TSC drones.
 * - Shows a boss health bar.
 */
public class TSCDroneBossEntity extends TSCDroneEntity {

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("Dr.K's Admin Drone Boss"),
            BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.PROGRESS
    );


    public TSCDroneBossEntity(EntityType<? extends TSCDroneEntity> type, Level level) {
        super(type, level);
        this.xpReward = 200;
    }

    public static AttributeSupplier.Builder createBossAttributes() {
        return TSCDroneEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 88.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));

        // Boss is always a grenade-throwing drone
        this.goalSelector.addGoal(2, new DroneGrenadeAttackGoal(this, 1.0D, 20)); // shoots tsc grenade every second

        this.goalSelector.addGoal(3, new DroneFlyingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));


        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Villager.class, true, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // Update boss bar health
            bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }}

    // Boss bar visibility handling
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        bossEvent.removeAllPlayers();
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false; // Boss drone ignores fall damage as well
    }
}


