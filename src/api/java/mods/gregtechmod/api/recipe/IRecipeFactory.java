package mods.gregtechmod.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeFactory {
    IRecipeCentrifuge makeCentrifugeRecipe(ItemStack input, List<ItemStack> outputs, int cells, int duration);

    default IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient input, ItemStack output, double energyCost, int duration) {
        return makeAssemblerRecipe(input, null, output, energyCost, duration);
    }
    IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, double energyCost, int duration);
}
