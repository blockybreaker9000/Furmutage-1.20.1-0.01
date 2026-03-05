package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.config.TransfurTeamHelper;
import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.entity.custom.PureWhiteLatexCrawlerEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Extra spawn mechanic for the Pure White Latex Crawler:
 * - 5% chance to spawn from any WHITE team entity's death (excluding the crawler itself).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PureWhiteLatexCrawlerSpawnEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }

        // Don't spawn from the crawler itself to avoid chains
        EntityType<?> type = entity.getType();
        if (type == ModEntities.PURE_WHITE_LATEX_CRAWLER.get()) {
            return;
        }

        int team = getEffectiveTeam(entity);
        if (team != TransfurTeamHelper.TEAM_WHITE) {
            return;
        }

        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        // 5% chance to spawn a crawler on death
        if (level.random.nextFloat() >= 0.05f) {
            return;
        }

        PureWhiteLatexCrawlerEntity crawler = ModEntities.PURE_WHITE_LATEX_CRAWLER.get().create(level);
        if (crawler == null) {
            return;
        }

        crawler.moveTo(entity.getX(), entity.getY(), entity.getZ(), level.random.nextFloat() * 360.0F, 0.0F);
        level.addFreshEntity(crawler);
    }

    private static int getEffectiveTeam(LivingEntity entity) {
        if (entity instanceof Player player) {
            return TransfurTeamHelper.getPlayerTransfurTeam(player);
        }
        var key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        String id = key != null ? key.toString() : "";
        return LatexTeamConfig.getTeamForEntity(id);
    }
}

