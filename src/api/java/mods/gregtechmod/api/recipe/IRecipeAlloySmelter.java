package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeAlloySmelter extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> {
    boolean isUniversal();
}
