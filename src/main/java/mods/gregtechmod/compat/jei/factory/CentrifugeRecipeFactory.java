package mods.gregtechmod.compat.jei.factory;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.recipe.RecipeCentrifuge;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;
import java.util.List;

public class CentrifugeRecipeFactory extends CellularRecipeFactory {
    public static final CentrifugeRecipeFactory INSTANCE = new CentrifugeRecipeFactory();

    @Nullable
    protected IRecipeCellular createCellRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost) {
        List<ItemStack> fluidCells = getCells(input);
        List<ItemStack> recipeOutput = GtUtil.copyList(output);

        duration = getDuration(duration, StackUtil.copyWithSize(fluidCells.get(0), count - cellCount), recipeOutput);
        if (duration < 0) return null;

        return constructCellRecipe(fluidCells, recipeOutput, count, Math.max(cellCount - count, 0), duration, energyCost);
    }

    protected IRecipeCellular constructCellRecipe(List<ItemStack> fluidCells, List<ItemStack> recipeOutput, int count, int cellCount, int duration, double energyCost) {
        return RecipeCentrifuge.create(RecipeIngredientItemStack.create(fluidCells, count), recipeOutput, cellCount, duration, CellType.CELL);
    }

    @Nullable
    protected IRecipeCellular createCanRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost) {
        List<ItemStack> fluidCells = getFluidContainers(input, ModHandler.can);
        List<ItemStack> recipeOutput = GtUtil.copyList(output);

        duration = getDuration(duration, StackUtil.copyWithSize(fluidCells.get(0), count), recipeOutput);
        if (duration < 0) return null;

        return constructCanRecipe(fluidCells, recipeOutput, count, cellCount, duration, energyCost);
    }

    protected IRecipeCellular constructCanRecipe(List<ItemStack> fluidCells, List<ItemStack> recipeOutput, int count, int cellCount, int duration, double energyCost) {
        return RecipeCentrifuge.create(RecipeIngredientItemStack.create(fluidCells, count), recipeOutput, cellCount, duration, CellType.CELL);
    }

    @Nullable
    protected IRecipeCellular createCapsuleRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost, ItemStack capsule) {
        List<ItemStack> capsules = getFluidContainers(input, capsule);
        List<ItemStack> recipeOutput = GtUtil.copyList(output);

        TileEntityIndustrialCentrifugeBase.addCellsToOutput(StackUtil.copyWithSize(capsules.get(0), count), recipeOutput);
        return constructCapsuleRecipe(capsules, recipeOutput, count, cellCount, duration, energyCost);
    }

    protected IRecipeCellular constructCapsuleRecipe(List<ItemStack> capsules, List<ItemStack> recipeOutput, int count, int cellCount, int duration, double energyCost) {
        return RecipeCentrifuge.create(RecipeIngredientItemStack.create(capsules, count), recipeOutput, cellCount, duration, CellType.CELL);
    }
}
