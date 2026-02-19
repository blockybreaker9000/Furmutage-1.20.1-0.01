package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntityGlassPaneCrawlEvents {

    private static final String CHANGED_NAMESPACE = "changed:";
    private static final String EXCLUDED_ROOMBA = "changed:roomba";
    private static final String EXCLUDED_EXOSKELETON = "changed:exoskeleton";
    private static final TagKey<Block> GLASS_PANE_TAG = BlockTags.create(new ResourceLocation("forge", "glass_pane"));
    private static final int GLASS_PANE_CRAWL_RANGE = 1;

    private static boolean isNearGlassPane(LivingEntity entity) {
        BlockPos center = entity.blockPosition();
        for (int dx = -GLASS_PANE_CRAWL_RANGE; dx <= GLASS_PANE_CRAWL_RANGE; dx++) {
            for (int dy = -GLASS_PANE_CRAWL_RANGE; dy <= GLASS_PANE_CRAWL_RANGE; dy++) {
                for (int dz = -GLASS_PANE_CRAWL_RANGE; dz <= GLASS_PANE_CRAWL_RANGE; dz++) {
                    BlockState state = entity.level().getBlockState(center.offset(dx, dy, dz));
                    Block block = state.getBlock();
                    if (state.is(GLASS_PANE_TAG) || block == Blocks.GLASS_PANE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide()) {
            return;
        }
        if (!entity.isAlive()) {
            return;
        }

        String entityType = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        if (!entityType.startsWith(CHANGED_NAMESPACE)) {
            return;
        }
        if (EXCLUDED_ROOMBA.equals(entityType) || EXCLUDED_EXOSKELETON.equals(entityType)) {
            return;
        }

        if (isNearGlassPane(entity)) {
            entity.setPose(Pose.SWIMMING);
        } else if (entity.getPose() == Pose.SWIMMING) {
            entity.setPose(Pose.STANDING);
        }
    }
}
