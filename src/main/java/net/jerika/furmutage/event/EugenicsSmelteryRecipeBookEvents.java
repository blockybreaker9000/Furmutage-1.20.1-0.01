package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraftforge.fml.common.Mod;

/**
 * Previously unlocked Eugenics Smeltery recipes in the recipe book when the player picked up
 * trigger items. Disabled to avoid "Unknown recipe category" warnings from ClientRecipeBook
 * (custom recipe types are not fully integrated with the recipe book category system).
 * Recipes still work in the Eugenics Smeltery; they just won't appear as "unlocked" in the book.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EugenicsSmelteryRecipeBookEvents {

    // Recipe book unlocking disabled to prevent "Unknown recipe category: furmutage:eugenics_smelting/..."
    // warnings. Eugenics smelting recipes still function in the smeltery.

    /*
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
        return item == ModItems.RAW_ROSELIGHT_NUGGET.get()
                || item == ModItems.THUNDERIUM_RAW_NUGGET.get()
                || item == ModItems.ROSELIGHT.get()
                || item == ModItems.THUNDERIUM.get();
    }
    */
}
