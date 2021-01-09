package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeFactory {
    IRecipeCentrifuge makeCentrifugeRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration);

    default IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient input, ItemStack output, double energyCost, int duration) {
        return makeAssemblerRecipe(input, null, output, duration, energyCost);
    }

    IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, int duration, double energyCost);

    default IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack output) {
        return makePulverizerRecipe(input, output, ItemStack.EMPTY, 0);
    }

    IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance);
}
