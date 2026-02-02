package net.jerika.furmutage.event;

import net.jerika.furmutage.config.ModCommonConfig;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple spawning for Changed white latex entities.
 * Uses same rules as zombies/skeletons: surface at night, caves (Monster.checkMonsterSpawnRules).
 * Config controls which entities spawn; only entities in the config list are allowed.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WhiteLatexSpawnEvents {

    private static final Set<String> MANAGED_ENTITY_IDS = Set.of(
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
            "changed:pure_white_latex_wolf_pup"
    );

    /**
     * Register spawn placements for entities in the config.
     * Uses simple Monster rules (surface at night, caves).
     */
    public static void registerSpawnPlacements() {
        if (!ModCommonConfig.ENABLE_WHITE_LATEX_SPAWN.get()) {
            return;
        }
        List<? extends String> enabled = ModCommonConfig.WHITE_LATEX_SPAWN_ENTITIES.get();
        if (enabled == null || enabled.isEmpty()) {
            return;
        }
        Set<String> enabledSet = new HashSet<>(enabled);

        for (String entityId : MANAGED_ENTITY_IDS) {
            if (!enabledSet.contains(entityId)) {
                continue;
            }
            ResourceLocation id = ResourceLocation.tryParse(entityId);
            if (id == null) continue;

            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
            if (type != null && type != EntityType.PLAYER && Mob.class.isAssignableFrom(type.getBaseClass())) {
                try {
                    @SuppressWarnings("unchecked")
                    EntityType<? extends Mob> mobType = (EntityType<? extends Mob>) type;
                    net.minecraft.world.entity.SpawnPlacements.register(
                            mobType,
                            net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
                            net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                            WhiteLatexSpawnEvents::checkSimpleMonsterSpawnRules
                    );
                    furmutage.LOGGER.debug("[WhiteLatexSpawn] Registered simple spawn rules for {}", entityId);
                } catch (Exception e) {
                    furmutage.LOGGER.warn("[WhiteLatexSpawn] Could not register spawn for {}: {}", entityId, e.getMessage());
                }
            }
        }
    }

    /** Simple spawn rules: surface at night, caves (same as zombies/skeletons). */
    private static <T extends Mob> boolean checkSimpleMonsterSpawnRules(EntityType<T> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        // Same as Monster: dark enough (hostile spawn light level)
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        if (skyLight > random.nextInt(32)) {
            return false;
        }
        if (blockLight > 0) {
            return false;
        }
        // Valid spawn block (solid surface)
        BlockPos below = pos.below();
        return level.getBlockState(below).isValidSpawn(level, below, type);
    }

    /**
     * Cancel natural spawns only (not spawn eggs or commands) for entities in our managed list when:
     * - White latex spawn is disabled, or
     * - Entity is not in the config list.
     */
    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getSpawnType() != MobSpawnType.NATURAL) {
            return;
        }

        String entityId = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType()).toString();
        if (!MANAGED_ENTITY_IDS.contains(entityId)) {
            return;
        }

        // If spawn system is disabled, cancel all managed entity natural spawns
        if (!ModCommonConfig.ENABLE_WHITE_LATEX_SPAWN.get()) {
            event.setSpawnCancelled(true);
            return;
        }

        // Check if entity is in config allowlist
        List<? extends String> enabled = ModCommonConfig.WHITE_LATEX_SPAWN_ENTITIES.get();
        if (enabled != null && enabled.contains(entityId)) {
            return;
        }

        // Entity is in our list but not in config - cancel natural spawn
        event.setSpawnCancelled(true);
    }
}
