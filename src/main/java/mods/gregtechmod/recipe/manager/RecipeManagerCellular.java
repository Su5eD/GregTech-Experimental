package mods.gregtechmod.recipe.manager;

import ic2.core.item.ItemFluidCell;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class RecipeManagerCellular extends RecipeManagerBase<IRecipeCellular> implements IGtRecipeManagerCellular {

    /**
     * @param cells The amount of cells required for this recipe. Pass in -1 to ignore
     */
    @Override
    public IRecipeCellular getRecipeFor(ItemStack input, int cells) {
        return this.recipes.stream()
                .filter(recipe -> {
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
                    return recipe.getInput().apply(input) && (cells < 0 || availableCells >= recipe.getCells());
                })
                .min(this::compareCount)
                .orElse(null);
    }

    @Override
    public IRecipeCellular getRecipeFor(@Nullable FluidStack input, int cells) {
        if (input == null) return null;

        return this.recipes.stream()
                .filter(recipe -> {
                    IRecipeIngredient ingredient = recipe.getInput();
                    if (ingredient instanceof IRecipeIngredientFluid) {
                        return ((IRecipeIngredientFluid) input).apply(input) && (cells < 0 || cells >= recipe.getCells());
                    }
                    return false;
                })
                .min(this::compareCount)
                .orElse(null);
    }

    @Override
    protected IRecipeCellular getRecipeForExact(IRecipeCellular recipe) {
        IRecipeIngredient input = recipe.getInput();
        int cells = recipe.getCells();
        return this.recipes.stream()
                .filter(r -> r.getInput().apply(input) && r.getCells() <= cells && compareCount(r, recipe) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean hasRecipeFor(FluidStack input) {
        return this.recipes.stream()
                .map(IMachineRecipe::getInput)
                .filter(ingredient -> ingredient instanceof IRecipeIngredientFluid)
                .anyMatch(ingredient -> ((IRecipeIngredientFluid) ingredient).apply(input));
    }

    @Override
    public boolean hasRecipeFor(Fluid input) {
        return this.recipes.stream()
                .map(IMachineRecipe::getInput)
                .filter(ingredient -> ingredient instanceof IRecipeIngredientFluid)
                .anyMatch(ingredient -> ((IRecipeIngredientFluid) ingredient).apply(input));
    }

    @Override
    public int compareCount(IRecipeCellular first, IRecipeCellular second) {
        int diff = second.getCells() - first.getCells();

        if (diff == 0) diff += super.compareCount(first, second);

        return diff;
    }
}
