package net.jerika.furmutage.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.ltxprogrammer.changed.entity.animation.StunAnimationParameters;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;

public abstract class TSCElectriumWeapon extends Item implements Vanishable {
    private final Cacheable<Multimap<Attribute, AttributeModifier>> defaultModifiers;

    public TSCElectriumWeapon(Properties properties) {
        super(properties);
        this.defaultModifiers = new Cacheable<>() {
            @Override
            protected Multimap<Attribute, AttributeModifier> initialGet() {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage(), AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed(), AttributeModifier.Operation.ADDITION));
                builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier("Weapon modifier", attackRange(), AttributeModifier.Operation.MULTIPLY_BASE));
                return builder.build();
            }
        };
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public int attackStun() { return 2; }
    public abstract double attackDamage();
    public abstract double attackSpeed();
    public double attackRange() {
        return 1.0;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers.get() : super.getDefaultAttributeModifiers(slot);
    }

    public static void sweepWeapon(LivingEntity source, double attackRange) {
        double d0 = (double)(-Mth.sin(source.getYRot() * ((float)Math.PI / 180F))) * attackRange;
        double d1 = (double)Mth.cos(source.getYRot() * ((float)Math.PI / 180F)) * attackRange;
        if (source.level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(ChangedParticles.TSC_SWEEP_ATTACK.get(),
                    source.getX() + d0, source.getY(0.5D),
                    source.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
    }

    public static void applyShock(LivingEntity enemy, int attackStun) {
        ChangedSounds.broadcastSound(enemy, ChangedSounds.TSC_WEAPON_SHOCK, 1, 1);
        enemy.addEffect(new MobEffectInstance(ChangedEffects.SHOCK.get(), attackStun, 100, false, false, true));
        enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 10, true, true, true));
        enemy.addEffect(new MobEffectInstance(MobEffects.WITHER, 50, 1, true, true, true));
        ChangedAnimationEvents.broadcastEntityAnimation(enemy, ChangedAnimationEvents.SHOCK_STUN.get(), StunAnimationParameters.INSTANCE);
    }

}
