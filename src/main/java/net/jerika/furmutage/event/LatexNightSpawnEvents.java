package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

/**
 * Registers spawn placements for certain Changed latex entities so they spawn at night
 * and in dark caves (up to 30 blocks below sea level). Also ensures they have AI enabled
 * when they spawn (fixes frozen entities).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexNightSpawnEvents {

    private static final Set<String> NIGHT_OR_DARK_ENTITY_IDS = Set.of(
            "changed:pure_white_latex_wolf",
            "changed:pure_white_latex_wolf_pup",
            "changed:latex_mutant_bloodcell_wolf",
            "changed:latex_snake",
            "changed:headless_knight",
            "changed:white_latex_knight_fusion",
            "changed:white_latex_wolf_female",
            "changed:white_latex_wolf_male",
            "changed:white_latex_knight",
            "changed:white_latex_centaur",
            "changed:milk_pudding",
            "changed:custom_latex"
    );

    private static final long NIGHT_START = 13000;
    private static final long NIGHT_END = 23000;
    private static final int MAX_CAVE_DEPTH_Y = 34;

    public static void registerSpawnPlacements() {
        for (String entityId : NIGHT_OR_DARK_ENTITY_IDS) {
            ResourceLocation id = ResourceLocation.tryParse(entityId);
            if (id == null) continue;

            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
            if (type == null || type == EntityType.PLAYER || !Mob.class.isAssignableFrom(type.getBaseClass())) {
                furmutage.LOGGER.debug("[LatexNightSpawn] {} not found or not a Mob, skipping", entityId);
                continue;
            }
            try {
                @SuppressWarnings("unchecked")
                EntityType<? extends Mob> mobType = (EntityType<? extends Mob>) type;
                net.minecraft.world.entity.SpawnPlacements.register(
                        mobType,
                        net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        LatexNightSpawnEvents::checkLatexNightSpawnRules
                );
            } catch (Exception e) {
                furmutage.LOGGER.warn("[LatexNightSpawn] Could not register spawn for {}: {}", entityId, e.getMessage());
            }
        }
    }

    /**
     * Spawn at night anywhere, or in dark areas (caves) up to 30 blocks below sea level.
     * Uses same dark/block checks as monster spawning.
     */
    private static <T extends Mob> boolean checkLatexNightSpawnRules(EntityType<T> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        if (!net.minecraft.world.entity.Mob.checkMobSpawnRules(type, level, reason, pos, random)) {
            return false;
        }
        long dayTime = level.getLevelData().getDayTime() % 24000;
        boolean isNight = dayTime >= NIGHT_START && dayTime < NIGHT_END;
        if (isNight) {
            int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
            int skyLight = level.getBrightness(LightLayer.SKY, pos);
            return blockLight <= 7 && skyLight <= 7;
        }
        int y = pos.getY();
        if (y < MAX_CAVE_DEPTH_Y) {
            return false;
        }
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return blockLight <= 0 && skyLight <= 7;
    }

    /**
     * Ensure AI is enabled when these entities spawn (fixes frozen entities).
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof Mob mob)) {
            return;
        }
        String entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        if (!NIGHT_OR_DARK_ENTITY_IDS.contains(entityId)) {
            return;
        }
        if (mob.isNoAi()) {
            mob.setNoAi(false);
        }
    }
}
