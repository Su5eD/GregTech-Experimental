package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface IRecipePrinter extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> {
    @Nullable
    IRecipeIngredient getCopyIngredient();
}
