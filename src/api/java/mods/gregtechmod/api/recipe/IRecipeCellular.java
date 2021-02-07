package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public interface IRecipeCellular extends IGtMachineRecipe<IRecipeIngredient, Collection<ItemStack>> {
    int getCells();
}
