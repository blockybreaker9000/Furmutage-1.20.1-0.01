package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

/**
 * Restricts certain Changed mod latex entities to spawn at night or in dark areas
 * (caves, etc.) like zombies. In caves, only spawns up to 30 blocks below sea level.
 * Does NOT remove entities that already exist during daytime.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexNightSpawnEvents {

    // Entities restricted to night or dark cave spawns
    private static final Set<String> NIGHT_OR_DARK_ENTITIES = Set.of(
            "changed:pure_white_latex_wolf",
            "changed:latex_mutant_bloodcell_wolf",
            "changed:latex_snake",
            "changed:headless_knight",
            "changed:white_latex_knight_fusion",
            "changed:white_latex_wolf_female",
            "changed:white_latex_wolf_male",
            "changed:white_latex_knight",
            "changed:white_latex_centaur",
            "changed:milk_pudding",
            "changed:pure_white_latex_wolf_pup",
            "changed:custom_latex"
    );

    // Night time range (ticks): 13000 = sunset, 23000 = sunrise (Minecraft day cycle)
    private static final long NIGHT_START = 13000;
    private static final long NIGHT_END = 23000;

    // Max depth for cave spawns: 30 blocks below sea level (Y=64) = Y 34
    private static final int MAX_CAVE_DEPTH_Y = 34;

    // Light level threshold for "dark" (hostile mob spawn threshold)
    private static final int DARK_LIGHT_THRESHOLD = 7;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        Entity entity = event.getEntity();
        String entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        if (!NIGHT_OR_DARK_ENTITIES.contains(entityId)) {
            return;
        }

        long dayTime = event.getLevel().getLevelData().getDayTime() % 24000;
        boolean isNight = dayTime >= NIGHT_START && dayTime < NIGHT_END;

        if (isNight) {
            return; // Allow at night, no removal
        }

        // Daytime: only allow if in a dark area (cave) and within depth limit
        BlockPos pos = entity.blockPosition();
        int blockLight = event.getLevel().getBrightness(LightLayer.BLOCK, pos);
        int skyLight = event.getLevel().getBrightness(LightLayer.SKY, pos);
        int maxLight = Math.max(blockLight, skyLight);
        boolean isDark = maxLight <= DARK_LIGHT_THRESHOLD;
        boolean withinDepth = entity.getY() >= MAX_CAVE_DEPTH_Y;

        if (isDark && withinDepth) {
            return; // Allow in dark cave within depth limit
        }

        // Daytime + bright or too deep: remove (invalid spawn)
        entity.remove(Entity.RemovalReason.DISCARDED);
    }
}
