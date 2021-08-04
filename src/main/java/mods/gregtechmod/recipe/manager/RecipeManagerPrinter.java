package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipePrinter;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeManagerPrinter extends RecipeManagerMultiInput<IRecipePrinter, IRecipeIngredient> {

    @Override
    public IRecipePrinter getRecipeFor(List<ItemStack> input) {
        return this.recipes.stream()
                .filter(recipe -> {
                    List<IRecipeIngredient> recipeInputs = recipe.getInput();
                    int size = recipeInputs.size();
                    if (size <= input.size()) {
                        for (int i = 0; i < size; i++) {
                            if (!recipeInputs.get(i).apply(input.get(i))) return false;
                        }
                        IRecipeIngredient copy = recipe.getCopyIngredient();
                        if (copy != null) return copy.apply(input.get(2));
                        return true;
                    }

                    return false;
                })
                .min(this::compareCount)
                .orElseGet(() -> getProvidedRecipe(input));
    }
}
