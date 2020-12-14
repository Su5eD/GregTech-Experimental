package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.api.recipe.IRecipeFactory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeFactory implements IRecipeFactory {

    @Override
    public IRecipeCentrifuge makeCentrifugeRecipe(ItemStack input, List<ItemStack> outputs, int cells, int duration) {
        return RecipeCentrifuge.create(input, outputs, cells, duration);
    }

    @Override
    public IGtMachineRecipe<List<ItemStack>, ItemStack> makeAssemblerRecipe(List<ItemStack> input, ItemStack output, double energyCost, int duration) {
        return RecipeAssembler.create(input, output, energyCost, duration);
    }
}
