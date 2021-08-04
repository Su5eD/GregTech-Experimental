package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeManagerSawmill extends RecipeManagerSecondaryFluid<IRecipeUniversal<List<IRecipeIngredient>>> {

    @Override
    public boolean addRecipe(IRecipeUniversal<List<IRecipeIngredient>> recipe, boolean overwrite) {
        boolean ret = super.addRecipe(recipe, overwrite);
        if (ret && recipe.isUniversal()) {
            List<ItemStack> outputs = recipe.getOutput();
            ModHandler.addTESawmillRecipe(1600, recipe.getInput().get(0).getMatchingInputs().get(0), outputs.get(0), outputs.size() > 1 ? outputs.get(1) : ItemStack.EMPTY, 100, false);
        }
        return ret;
    }
}
