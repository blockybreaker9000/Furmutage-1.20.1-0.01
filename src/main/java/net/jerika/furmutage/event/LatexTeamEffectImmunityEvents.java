package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.config.TransfurTeamHelper;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Prevents Spore mod effects from affecting white latex team (transfurred players + white latex entities).
 * Prevents The Flesh That Hates mod effects from affecting dark latex team (transfurred players + dark latex entities).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexTeamEffectImmunityEvents {

    private static final String SPORE_NAMESPACE = "spore";
    private static final String FLESH_THAT_HATES_NAMESPACE = "the_flesh_that_hates";

    /** Effective team: 0 = none/human, 1 = white, 2 = dark. */
    private static int getEffectiveTeam(LivingEntity entity) {
        if (entity == null) return 0;
        if (entity instanceof Player player) return TransfurTeamHelper.getPlayerTransfurTeam(player);
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return LatexTeamConfig.getTeamForEntity(key.toString());
    }

    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;

        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null) return;

        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(instance.getEffect());
        if (effectId == null) return;

        String namespace = effectId.getNamespace();

        int team = getEffectiveTeam(entity);

        // Spore mod effects: do not affect white team (1)
        if (SPORE_NAMESPACE.equals(namespace) && team == TransfurTeamHelper.TEAM_WHITE) {
            event.setResult(Event.Result.DENY);
            return;
        }

        // The Flesh That Hates mod effects: do not affect dark team (2)
        if (FLESH_THAT_HATES_NAMESPACE.equals(namespace) && team == TransfurTeamHelper.TEAM_DARK) {
            event.setResult(Event.Result.DENY);
        }
    }
}
