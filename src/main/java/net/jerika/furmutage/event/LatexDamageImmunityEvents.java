package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.config.TransfurTeamHelper;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
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
 * Also: The Flesh That Hates mod damage (blocks, etc.) does not affect dark team.
 * Spore mod damage (blocks, etc.) does not affect white team (white transfurred players and white latex entities).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexDamageImmunityEvents {

    private static final String FLESH_THAT_HATES_NAMESPACE = "the_flesh_that_hates";
    private static final String SPORE_NAMESPACE = "spore";

    private static final TagKey<DamageType> LATEX_IMMUNE_TO = TagKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation("changed", "latex_immune_to")
    );

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;

        DamageSource source = event.getSource();
        if (source == null) return;

        // Never interfere with Changed's own damage to players (including transfur kill logic).
        // If the damage type's namespace is "changed" and the target is a player, let Changed handle it.
        if (entity instanceof Player && isChangedDamage(source)) {
            return;
        }

        // Changed latex_immune_to tag: all latex/transfurred take no damage from those types
        if (isLatexEntity(entity) && isDamageInLatexImmuneTag(source, entity)) {
            event.setCanceled(true);
            return;
        }

        // The Flesh That Hates: dark team takes no damage from that mod's damage types (e.g. blocks)
        if (isDarkTeam(entity) && isDamageFromFleshThatHates(source)) {
            event.setCanceled(true);
            return;
        }

        // Spore: white team takes no damage from that mod's damage types (e.g. blocks, infection)
        if (isWhiteTeam(entity) && isDamageFromSpore(source)) {
            event.setCanceled(true);
        }
    }

    private static boolean isDamageFromFleshThatHates(DamageSource source) {
        return isDamageFromNamespace(source, FLESH_THAT_HATES_NAMESPACE);
    }

    private static boolean isDamageFromSpore(DamageSource source) {
        return isDamageFromNamespace(source, SPORE_NAMESPACE);
    }

    private static boolean isDamageFromNamespace(DamageSource source, String namespace) {
        return source.typeHolder().unwrapKey()
                .map(ResourceKey::location)
                .map(ResourceLocation::getNamespace)
                .filter(namespace::equals)
                .isPresent();
    }

    private static boolean isDarkTeam(LivingEntity entity) {
        if (entity == null) return false;
        if (entity instanceof Player player) return TransfurTeamHelper.getPlayerTransfurTeam(player) == TransfurTeamHelper.TEAM_DARK;
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return LatexTeamConfig.getTeamForEntity(key.toString()) == TransfurTeamHelper.TEAM_DARK;
    }

    private static boolean isWhiteTeam(LivingEntity entity) {
        if (entity == null) return false;
        if (entity instanceof Player player) return TransfurTeamHelper.getPlayerTransfurTeam(player) == TransfurTeamHelper.TEAM_WHITE;
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return LatexTeamConfig.getTeamForEntity(key.toString()) == TransfurTeamHelper.TEAM_WHITE;
    }

    private static boolean isDamageInLatexImmuneTag(DamageSource source, LivingEntity entity) {
        if (source == null || entity.level() == null) return false;
        var registry = entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).orElse(null);
        if (registry == null) return false;
        var tag = registry.getTag(LATEX_IMMUNE_TO).orElse(null);
        if (tag == null) return false;
        return tag.contains(source.typeHolder());
    }

    private static boolean isChangedDamage(DamageSource source) {
        return source.typeHolder().unwrapKey()
                .map(ResourceKey::location)
                .map(ResourceLocation::getNamespace)
                .filter(ns -> "changed".equals(ns))
                .isPresent();
    }

    private static boolean isLatexEntity(LivingEntity entity) {
        // IMPORTANT: Do NOT treat players as "latex entities" for the latex_immune_to tag.
        // Changed already handles player transfur and its own kill logic; if we treat
        // players as latex here, we can accidentally cancel the damage that kills them
        // at the end of a transfur. Limit this check to non-player latex mobs only.
        if (entity instanceof Player) {
            return false;
        }
        String type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        return LatexTeamConfig.isEntityInTeam(type);
    }
}
