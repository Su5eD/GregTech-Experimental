package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeSawmill;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSawmill;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class RecipeManagerSawmill extends RecipeManagerBase<IRecipeSawmill> implements IGtRecipeManagerSawmill {

    @Override
    public boolean addRecipe(IRecipeSawmill recipe, boolean overwrite) {
        boolean ret = super.addRecipe(recipe, overwrite);
        if (ret) {
            List<ItemStack> outputs = recipe.getOutput();
            ModHandler.addTESawmillRecipe(1600, recipe.getInput().getMatchingInputs().get(0), outputs.get(0), outputs.size() > 1 ? outputs.get(1) : ItemStack.EMPTY, 100, false);
        }
        return ret;
    }

    @Override
    public IRecipeSawmill getRecipeFor(ItemStack input, @Nullable FluidStack fluid) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(input) && (fluid == null || fluid.containsFluid(recipe.getRequiredWater())))
                .min(this::compareCount)
                .orElse(getProvidedRecipe(input));
    }
}
