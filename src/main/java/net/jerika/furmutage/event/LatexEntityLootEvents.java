package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

/**
 * Adds chance-based nugget drops when white latex or dark latex entities die.
 * White latex: 25% chance to drop raw thunderium nugget.
 * Dark latex: 30% chance to drop raw roselight nugget.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexEntityLootEvents {

    private static final double WHITE_LATEX_DROP_CHANCE = 0.25;
    private static final double DARK_LATEX_DROP_CHANCE = 0.30;

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }

        String entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        Set<String> whiteLatex = LatexTeamConfig.getWhiteLatexEntities();
        Set<String> darkLatex = LatexTeamConfig.getDarkLatexEntities();

        if (whiteLatex.contains(entityTypeId)) {
            if (entity.getRandom().nextDouble() < WHITE_LATEX_DROP_CHANCE) {
                var item = ModItems.THUNDERIUM_RAW_NUGGET.get();
                var level = entity.level();
                if (item != null && level != null) {
                    event.getDrops().add(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(),
                            new ItemStack(item)));
                }
            }
        } else if (darkLatex.contains(entityTypeId)) {
            if (entity.getRandom().nextDouble() < DARK_LATEX_DROP_CHANCE) {
                var item = ModItems.RAW_ROSELIGHT_NUGGET.get();
                var level = entity.level();
                if (item != null && level != null) {
                    event.getDrops().add(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(),
                            new ItemStack(item)));
                }
            }
        }
    }
}
