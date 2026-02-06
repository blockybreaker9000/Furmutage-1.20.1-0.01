package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers spawn placement for Changed mod wolf_crystal so it can spawn
 * and be placed (e.g. spawn egg) onto furmutage:tainted_dark_grass.
 */
public class WolfCrystalSpawnEvents {

    private static final ResourceLocation WOLF_CRYSTAL_ID = ResourceLocation.tryParse("changed:wolf_crystal");

    public static void registerSpawnPlacements() {
        if (WOLF_CRYSTAL_ID == null) {
            return;
        }
        EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(WOLF_CRYSTAL_ID);
        if (type == null || type == EntityType.PLAYER || !Mob.class.isAssignableFrom(type.getBaseClass())) {
            furmutage.LOGGER.debug("[WolfCrystalSpawn] changed:wolf_crystal entity type not found or not a Mob, skipping spawn placement");
            return;
        }
        try {
            @SuppressWarnings("unchecked")
            EntityType<? extends Mob> mobType = (EntityType<? extends Mob>) type;
            net.minecraft.world.entity.SpawnPlacements.register(
                    mobType,
                    net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    WolfCrystalSpawnEvents::checkWolfCrystalSpawnRules
            );
            furmutage.LOGGER.debug("[WolfCrystalSpawn] Registered spawn placement for changed:wolf_crystal (allowed on tainted_dark_grass)");
        } catch (Exception e) {
            furmutage.LOGGER.warn("[WolfCrystalSpawn] Could not register spawn for changed:wolf_crystal: {}", e.getMessage());
        }
    }

    /**
     * Allow spawn when: (1) spawn egg / dispenser / command, or (2) natural spawn on tainted_dark_grass.
     */
    private static <T extends Mob> boolean checkWolfCrystalSpawnRules(EntityType<T> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        BlockPos below = pos.below();
        return level.getBlockState(below).is(ModBlocks.TAINTED_DARK_GRASS.get());
    }
}
