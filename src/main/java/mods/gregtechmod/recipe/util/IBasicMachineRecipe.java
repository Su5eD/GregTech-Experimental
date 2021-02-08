package mods.gregtechmod.recipe.util;

import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;

public interface IBasicMachineRecipe {
    IRecipeInput getInput();

    ItemStack getOutput();
}
