package net.jerika.furmutage.item;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier ROSELIGHT = TierSortingRegistry.registerTier(
            new ForgeTier(5, 800, 10f, 0.1f, 25,
                    ModTags.Blocks.NEEDS_ROSELIGHT_TOOL, () -> Ingredient.of(ModItems.ROSELIGHT.get())),
            new ResourceLocation(furmutage.MOD_ID, "roselight"), List.of(Tiers.DIAMOND), List.of());
    public static final Tier THUNDERIUM = TierSortingRegistry.registerTier(
            new ForgeTier(5, 800, 5f, 1f, 25,
                    ModTags.Blocks.NEEDS_THUNDERIUM_TOOL, () -> Ingredient.of(ModItems.THUNDERIUM.get())),
            new  ResourceLocation(furmutage.MOD_ID, "thunderium"), List.of(Tiers.DIAMOND), List.of());

}
