package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.jerika.furmutage.recipe.EugenicsSmeltingRecipe;
import net.jerika.furmutage.recipe.ModRecipeTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Unlocks Eugenics Smeltery recipes in the recipe book when the player picks up
 * raw roselight, raw thunderium, roselight, thunderium, their raw nuggets, or TSC plastic.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EugenicsSmelteryRecipeBookEvents {

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }
        ItemStack pickedUp = event.getItem().getItem();
        if (pickedUp.isEmpty()) {
            return;
        }
        if (!isEugenicsSmelteryTriggerItem(pickedUp)) {
            return;
        }
        var recipeManager = serverPlayer.level().getRecipeManager();
        List<EugenicsSmeltingRecipe> allEugenics = recipeManager.getAllRecipesFor(ModRecipeTypes.EUGENICS_SMELTING.get());
        List<Recipe<?>> toUnlock = new ArrayList<>();
        for (EugenicsSmeltingRecipe recipe : allEugenics) {
            if (!recipe.getIngredients().isEmpty() && recipe.getIngredients().get(0).test(pickedUp)) {
                toUnlock.add(recipe);
            }
        }
        if (!toUnlock.isEmpty()) {
            serverPlayer.awardRecipes(toUnlock);
        }
    }

    private static boolean isEugenicsSmelteryTriggerItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        var item = stack.getItem();
        return item == ModBlocks.RAW_ROSELIGHT.get().asItem()
                || item == ModBlocks.RAW_THUNDERIUM.get().asItem()
                || item == ModItems.ROSELIGHT.get()
                || item == ModItems.THUNDERIUM.get()
                || item == ModItems.RAW_ROSELIGHT_NUGGET.get()
                || item == ModItems.THUNDERIUM_RAW_NUGGET.get()
                || item == ModItems.TSC_PLASTIC_WASTE_CLUMP.get();
    }
}
