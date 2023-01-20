package dev.su5ed.gtexperimental.api.recipe;

import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IRecipeCellular extends IMachineRecipe<IRecipeIngredient, List<ItemStack>> {
    int getCells();

    CellType getCellType();
}
