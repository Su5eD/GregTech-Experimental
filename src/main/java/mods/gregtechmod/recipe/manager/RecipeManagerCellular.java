package mods.gregtechmod.recipe.manager;

import ic2.core.item.ItemFluidCell;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Comparator;
import java.util.stream.Stream;

public class RecipeManagerCellular extends RecipeManagerBase<IRecipeCellular> implements IGtRecipeManagerCellular {

    public RecipeManagerCellular() {
        super(new CellularRecipeComparator());
    }

    /**
     * @param cells The amount of cells required for this recipe. Pass in -1 to ignore
     */
    @Override
    public IRecipeCellular getRecipeFor(ItemStack input, int cells) {
        for (IRecipeCellular recipe : this.recipes) {
            int availableCells = cells;
            IRecipeIngredient ingredient = recipe.getInput();
            if (ingredient instanceof IRecipeIngredientFluid) {
                FluidStack fluid = FluidUtil.getFluidContained(input);
                if (fluid != null) {
                    Item item = input.getItem();
                    if (fluid.amount == Fluid.BUCKET_VOLUME && item instanceof ItemFluidCell) {
                        availableCells += Math.min(ingredient.getCount(), input.getCount());
                    }
                }
            }
            if (recipe.getInput().apply(input) && (cells < 0 || availableCells >= recipe.getCells())) return recipe;
        }
        return null;
    }

    @Override
    public IRecipeCellular getRecipeFor(FluidStack input, int cells) {
        return this.recipes.stream()
                .filter(recipe -> Stream.of(recipe)
                            .map(IGtMachineRecipe::getInput)
                            .allMatch(ingredient -> ingredient instanceof IRecipeIngredientFluid && ((IRecipeIngredientFluid) ingredient).apply(input) && (cells < 0 || cells >= recipe.getCells())))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean hasRecipeFor(FluidStack input) {
        return this.recipes.stream()
                .map(IGtMachineRecipe::getInput)
                .filter(ingredient -> ingredient instanceof IRecipeIngredientFluid)
                .anyMatch(ingredient -> ((IRecipeIngredientFluid) ingredient).apply(input));
    }

    @Override
    public boolean hasRecipeFor(Fluid input) {
        return this.recipes.stream()
                .map(IGtMachineRecipe::getInput)
                .filter(ingredient -> ingredient instanceof IRecipeIngredientFluid)
                .anyMatch(ingredient -> ((IRecipeIngredientFluid) ingredient).apply(input));
    }

    private static class CellularRecipeComparator implements Comparator<IRecipeCellular> {

        @Override
        public int compare(IRecipeCellular first, IRecipeCellular second) {
            int itemdiff = second.getCells() - first.getCells();

            if (itemdiff == 0) itemdiff += first.getInput().compareTo(second.getInput());

            return itemdiff;
        }
    }
}
