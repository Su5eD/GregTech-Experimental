package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class RecipeManagerCellular extends RecipeManagerBase<IRecipeCellular> implements IGtRecipeManagerCellular {

    /**
     * @param cell The cell ItemStack. Pass in <code>null</code> to ignore
     */
    @Override
    public IRecipeCellular getRecipeFor(ItemStack input, @Nullable ItemStack cell) {
        return this.recipes.stream()
                .filter(recipe -> {
                    if (recipe.getInput().apply(input)) {
                        if (cell != null) {
                            int availableCells = cell.getCount();
                            IRecipeIngredient ingredient = recipe.getInput();
                            if (ingredient instanceof IRecipeIngredientFluid) {
                                FluidStack fluid = FluidUtil.getFluidContained(input);
                                if (fluid != null) {
                                    if (fluid.amount == Fluid.BUCKET_VOLUME && TileEntityIndustrialCentrifugeBase.isIC2Cell(input.getItem())) {
                                        availableCells += Math.min(ingredient.getCount(), input.getCount());
                                    }
                                }
                            }
                            return (cell.isEmpty() || recipe.getCellType().apply(cell, GregTechMod.classic)) && availableCells >= recipe.getCells();
                        }
                        return true;
                    }
                    return false;
                })
                .min(this::compareCount)
                .orElseGet(() -> getProvidedRecipe(input));
    }

    @Override
    public IRecipeCellular getRecipeFor(@Nullable FluidStack input, int cells) {
        if (input == null) return null;

        return this.recipes.stream()
                .filter(recipe -> {
                    IRecipeIngredient ingredient = recipe.getInput();
                    if (ingredient instanceof IRecipeIngredientFluid) {
                        return ((IRecipeIngredientFluid) ingredient).apply(input) && (cells < 0 || cells >= recipe.getCells());
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
        CellType cellType = recipe.getCellType();
        return this.recipes.stream()
                .filter(r -> r.getInput().apply(input) && r.getCells() <= cells && r.getCellType() == cellType && compareCount(r, recipe) == 0)
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
