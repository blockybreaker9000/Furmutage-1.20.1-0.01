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

    // TSC Emergency: 20% less durability than iron (200), 20% slower than gold pickaxe (9.6 speed), can mine iron (level 2), cannot mine diamond
    public static final Tier TSC_EMERGENCY = TierSortingRegistry.registerTier(
            new ForgeTier(2, 200, 9.6f, 1f, 12,
                    ModTags.Blocks.NEEDS_TSC_EMERGENCY_TOOL, () -> Ingredient.of(ModItems.TSC_PLASTIC_BAR.get())),
            new ResourceLocation(furmutage.MOD_ID, "tsc_emergency"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

    // Roselight Glass: can mine gold (level 2), 20% more durability than iron (300), 10% faster than iron (6.6 speed)
    public static final Tier ROSELIGHT_GLASS = TierSortingRegistry.registerTier(
            new ForgeTier(2, 300, 6.6f, 2f, 14,
                    ModTags.Blocks.NEEDS_ROSELIGHT_GLASS_TOOL, () -> Ingredient.of(ModItems.ROSELIGHT_GLASS_ROD.get())),
            new ResourceLocation(furmutage.MOD_ID, "roselight_glass"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

}
