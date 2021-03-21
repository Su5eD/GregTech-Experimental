package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeCellular extends IMachineRecipe<IRecipeIngredient, List<ItemStack>> {
    int getCells();

    CellType getCellType();
}
