package dev.su5ed.gtexperimental.api.recipe;

import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IRecipePrinter extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> {
    @Nullable
    IRecipeIngredient getCopyIngredient();
}
