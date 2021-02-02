package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.*;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeFactory implements IRecipeFactory {

    @Override
    public IRecipeCentrifuge makeCentrifugeRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration) {
        return RecipeCentrifuge.create(input, outputs, cells, duration);
    }

    @Override
    public IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, int duration, double energyCost) {
        return RecipeAssembler.create(primaryInput, secondaryInput, output, duration, energyCost);
    }

    @Override
    public IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        return RecipePulverizer.create(input, primaryOutput, secondaryOutput, chance);
    }

    @Override
    public IRecipeGrinder makeGrinderRecipe(IRecipeIngredient input, IRecipeIngredientFluid fluid, List<ItemStack> output, int duration) {
        return RecipeGrinder.create(input, fluid, output, duration);
    }

    @Override
    public IRecipeBlastFurnace makeBlastFurnaceRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost, int heat) {
        return RecipeBlastFurnace.create(input, output, duration, energyCost, heat);
    }
}
