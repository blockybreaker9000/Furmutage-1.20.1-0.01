package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.config.TransfurTeamHelper;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Applies the {@code changed:latex_immune_to} damage type tag: latex entities and
 * transfurred players take no damage from any damage type listed in that tag.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexDamageImmunityEvents {

    private static final TagKey<DamageType> LATEX_IMMUNE_TO = TagKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation("changed", "latex_immune_to")
    );

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;

        if (!isLatexEntity(entity)) return;
        if (!isDamageInLatexImmuneTag(event.getSource(), entity)) return;

        event.setCanceled(true);
    }

    private static boolean isDamageInLatexImmuneTag(net.minecraft.world.damagesource.DamageSource source, LivingEntity entity) {
        if (source == null || entity.level() == null) return false;
        var registry = entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).orElse(null);
        if (registry == null) return false;
        var tag = registry.getTag(LATEX_IMMUNE_TO).orElse(null);
        if (tag == null) return false;
        return tag.contains(source.typeHolder());
    }

    private static boolean isLatexEntity(LivingEntity entity) {
        if (entity instanceof Player player) {
            return TransfurTeamHelper.getPlayerTransfurTeam(player) != TransfurTeamHelper.TEAM_NONE;
        }
        String type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        return LatexTeamConfig.isEntityInTeam(type);
    }
}
