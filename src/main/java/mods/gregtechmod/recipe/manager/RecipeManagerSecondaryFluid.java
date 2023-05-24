package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.BiPredicate;

public class RecipeManagerSecondaryFluid<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends RecipeManagerMultiInput<R, IRecipeIngredient> implements IGtRecipeManagerSecondaryFluid<R> {

    @Override
    public boolean hasRecipeForPrimaryInput(ItemStack input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().get(0).apply(input, false));
    }

    @Override
    public R getRecipeFor(List<ItemStack> input) {
        ItemStack primary = input.get(0);
        ItemStack secondary = input.get(1);
        return getRecipeFor(primary, secondary, IRecipeIngredientFluid::apply);
    }

    @Override
    public R getRecipeFor(ItemStack input, FluidStack fluid) {
        return getRecipeFor(input, fluid, IRecipeIngredientFluid::apply);
    }

    @Override
    public boolean hasRecipeFor(Fluid input) {
        return this.recipes.stream()
            .anyMatch(recipe -> ((IRecipeIngredientFluid) recipe.getInput().get(1)).apply(input));
    }

    private <T> R getRecipeFor(ItemStack primaryInput, T secondaryInput, BiPredicate<IRecipeIngredientFluid, T> secondaryInputPred) {
        return this.getRecipes().stream()
            .filter(recipe -> {
                List<IRecipeIngredient> inputs = recipe.getInput();
                IRecipeIngredientFluid inputFluid = (IRecipeIngredientFluid) inputs.get(1);
                return inputs.get(0).apply(primaryInput) && secondaryInputPred.test(inputFluid, secondaryInput);
            })
            .min(this::compareCount)
            .orElse(null);
    }
}
