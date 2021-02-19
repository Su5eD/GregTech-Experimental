package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeGrinder;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerGrinder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Comparator;

public class RecipeManagerGrinder extends RecipeManagerBase<IRecipeGrinder> implements IGtRecipeManagerGrinder {

    public RecipeManagerGrinder() {
        super(new GrinderRecipeComparator());
    }

    @Override
    public IRecipeGrinder getRecipeFor(ItemStack stack, FluidStack fluid) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(stack) && recipe.getFluid().apply(fluid))
                .findFirst()
                .orElse(null);
    }

    private static class GrinderRecipeComparator implements Comparator<IRecipeGrinder> {

        @Override
        public int compare(IRecipeGrinder first, IRecipeGrinder second) {
            int diff = first.getInput().compareTo(second.getInput());
            if (diff == 0) diff += first.getFluid().compareTo(second.getFluid());
            return diff;
        }
    }
}
