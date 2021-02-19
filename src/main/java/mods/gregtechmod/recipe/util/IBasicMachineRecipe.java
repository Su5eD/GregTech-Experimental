package mods.gregtechmod.recipe.util;

import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IBasicMachineRecipe {
    IRecipeInput getInput();

    List<ItemStack> getOutput();

    boolean shouldOverwrite();
}
