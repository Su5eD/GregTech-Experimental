package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import net.minecraft.item.ItemStack;

public class RecipeManagerBasic<R extends IGtMachineRecipe<ItemStack, ?>> extends RecipeManager<ItemStack, R> {

    @Override
    public R getRecipeFor(ItemStack input) {
        for (R recipe : this.recipes) {
            if (recipe.getInput().isItemEqual(input)) return recipe;
        }
        return null;
    }
}
