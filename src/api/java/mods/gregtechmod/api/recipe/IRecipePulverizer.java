package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipePulverizer extends IGtMachineRecipe<IRecipeIngredient, List<ItemStack>> {
    ItemStack getPrimaryOutput();

    ItemStack getSecondaryOutput();

    int getChance();
}
