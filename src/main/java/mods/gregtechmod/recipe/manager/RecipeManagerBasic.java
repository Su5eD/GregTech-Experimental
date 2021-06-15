package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

public class RecipeManagerBasic<R extends IMachineRecipe<IRecipeIngredient, ?>> extends RecipeManagerBase<R> implements IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, R> {

    @Override
    public R getRecipeFor(ItemStack input) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(input))
                .min(this::compareCount)
                .orElse(getProvidedRecipe(input));
    }
}
