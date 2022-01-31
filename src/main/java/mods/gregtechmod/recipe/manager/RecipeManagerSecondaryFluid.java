package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class RecipeManagerSecondaryFluid<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends RecipeManagerMultiInput<R, IRecipeIngredient> implements IGtRecipeManagerSecondaryFluid<R> {

    @Override
    public boolean hasRecipeForPrimaryInput(ItemStack input) {
        return this.recipes.stream()
                .anyMatch(recipe -> recipe.getInput().get(0).apply(input, false));
    }

    @Override
    public R getRecipeFor(List<ItemStack> input, @Nullable FluidStack fluid) {
        ItemStack primary = input.get(0);
        ItemStack secondary = input.get(1);
        return this.getRecipes().stream()
                .filter(recipe -> {
                    List<IRecipeIngredient> inputs = recipe.getInput();
                    IRecipeIngredientFluid inputFluid = (IRecipeIngredientFluid) inputs.get(1);
                    return inputs.get(0).apply(primary) && (fluid != null ? inputFluid.apply(fluid) : inputFluid.apply(secondary));
                })
                .min(this::compareCount)
                .orElseGet(() -> getProvidedRecipe(input));
    }
}
