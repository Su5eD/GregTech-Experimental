package mods.gregtechmod.api.recipe.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public interface IRecipeIngredient {
    Ingredient asIngredient();

    int getCount();

    default boolean apply(ItemStack stack) {
        return apply(stack, true);
    }

    boolean apply(ItemStack stack, boolean checkCount);

    boolean apply(IRecipeIngredient ingredient);

    List<ItemStack> getMatchingInputs();

    boolean isEmpty();
}
