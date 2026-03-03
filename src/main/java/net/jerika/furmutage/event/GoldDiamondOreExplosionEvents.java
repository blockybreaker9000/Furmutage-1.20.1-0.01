package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;

/**
 * Prevents vanilla gold and diamond ores from being destroyed by explosions.
 * This effectively gives them obsidian-like blast resistance while leaving
 * all other blocks (including Furmutage ores) unchanged.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoldDiamondOreExplosionEvents {

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Level level = event.getLevel();

        Iterator<BlockPos> it = event.getAffectedBlocks().iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
            if (key == null) continue;

            String id = key.toString();
            if (isProtectedOre(id)) {
                // Remove from affected list so the explosion does not break this ore block
                it.remove();
            }
        }
    }

    private static boolean isProtectedOre(String id) {
        return id.equals("minecraft:gold_ore")
                || id.equals("minecraft:deepslate_gold_ore")
                || id.equals("minecraft:diamond_ore")
                || id.equals("minecraft:deepslate_diamond_ore");
    }
}

