package net.jerika.furmutage.util;

import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_ROSELIGHT_TOOL = tag("needs_roselight_tool");
        public static final TagKey<Block> NEEDS_THUNDERIUM_TOOL = tag("needs_thunderium_tool");
        public static final TagKey<Block> NEEDS_TSC_EMERGENCY_TOOL = tag("needs_tsc_emergency_tool");
        public static final TagKey<Block> NEEDS_ROSELIGHT_GLASS_TOOL = tag("needs_roselight_glass_tool");
        /** Blocks that white latex cover can live on (spread onto and not decay). */
        public static final TagKey<Block> TAINTED_WHITE = tag("tainted_white");
        /** Blocks that dark latex cover can live on (spread onto and not decay). */
        public static final TagKey<Block> TAINTED_DARK = tag("tainted_dark");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(furmutage.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(furmutage.MOD_ID, name));
        }
    }
}

