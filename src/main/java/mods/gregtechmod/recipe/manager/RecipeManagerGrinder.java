package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeGrinder;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerGrinder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeManagerGrinder extends RecipeManagerBase<IRecipeGrinder> implements IGtRecipeManagerGrinder {

    @Override
    protected IRecipeGrinder getRecipeForExact(IRecipeGrinder recipe) {
        return this.recipes.stream()
                .filter(r -> r.getInput().apply(recipe.getInput()) && r.getFluid().apply(recipe.getFluid()) && compareCount(r, recipe) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public IRecipeGrinder getRecipeFor(ItemStack stack, FluidStack fluid) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(stack) && recipe.getFluid().apply(fluid))
                .min(this::compareCount)
                .orElse(null);
    }
}
