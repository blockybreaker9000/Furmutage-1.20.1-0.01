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

