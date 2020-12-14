package mods.gregtechmod.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeFactory {
    IRecipeCentrifuge makeCentrifugeRecipe(ItemStack input, List<ItemStack> outputs, int cells, int duration);

    IGtMachineRecipe<List<ItemStack>, ItemStack> makeAssemblerRecipe(List<ItemStack> input, ItemStack output, double energyCost, int duration);
}
