package net.jerika.furmutage.event;

import net.jerika.furmutage.config.LatexTeamConfig;
import net.jerika.furmutage.config.TransfurTeamHelper;
import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Adds extra "digested" drops to latex team entities.
 * - White team: white digested rotten flesh + bones (50% chance), white digested gunpowder (2% chance).
 * - Dark team: dark digested rotten flesh + bones (50% chance), dark digested gunpowder (2% chance).
 * The latex mutant bomber has its own loot table and is excluded here.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LatexDigestedDropsEvents {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }

        // Do not alter the bomber's custom loot
        EntityType<?> type = entity.getType();
        if (type == ModEntities.LATEX_MUTANT_BOMBER.get()) {
            return;
        }

        int team = getEffectiveTeam(entity);
        if (team == TransfurTeamHelper.TEAM_NONE) {
            return;
        }

        var random = entity.getRandom();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (team == TransfurTeamHelper.TEAM_WHITE) {
            // 50% chance: extra white digested rotten flesh + bones
            if (random.nextFloat() < 0.5f) {
                addDrop(event, new ItemStack(ModItems.WHITE_DIGESTED_ROTTEN_FLESH.get()), x, y, z);
                addDrop(event, new ItemStack(ModItems.WHITE_DIGESTED_BONES.get()), x, y, z);
            }
            // 2% chance: white digested gunpowder
            if (random.nextFloat() < 0.02f) {
                addDrop(event, new ItemStack(ModItems.WHITE_DIGESTED_GUNPOWDER.get()), x, y, z);
            }
        } else if (team == TransfurTeamHelper.TEAM_DARK) {
            // 50% chance: extra dark digested rotten flesh + bones
            if (random.nextFloat() < 0.5f) {
                addDrop(event, new ItemStack(ModItems.DARK_DIGESTED_ROTTEN_FLESH.get()), x, y, z);
                addDrop(event, new ItemStack(ModItems.DARK_DIGESTED_BONES.get()), x, y, z);
            }
            // 2% chance: dark digested gunpowder
            if (random.nextFloat() < 0.02f) {
                addDrop(event, new ItemStack(ModItems.DARK_DIGESTED_GUNPOWDER.get()), x, y, z);
            }
        }
    }

    private static int getEffectiveTeam(LivingEntity entity) {
        if (entity instanceof Player player) {
            return TransfurTeamHelper.getPlayerTransfurTeam(player);
        }
        var key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        String id = key != null ? key.toString() : "";
        return LatexTeamConfig.getTeamForEntity(id);
    }

    private static void addDrop(LivingDropsEvent event, ItemStack stack, double x, double y, double z) {
        if (stack.isEmpty()) return;
        ItemEntity item = new ItemEntity(event.getEntity().level(), x, y, z, stack);
        item.setDefaultPickUpDelay();
        event.getDrops().add(item);
    }
}

