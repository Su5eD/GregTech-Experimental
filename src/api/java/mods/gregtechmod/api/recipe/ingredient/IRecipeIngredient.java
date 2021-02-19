package mods.gregtechmod.api.recipe.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.List;

public interface IRecipeIngredient extends Comparable<IRecipeIngredient> {
    Ingredient asIngredient();

    int getCount();

    default boolean apply(@Nullable ItemStack stack) {
        return apply(stack, true);
    }

    boolean apply(@Nullable ItemStack stack, boolean checkCount);

    List<ItemStack> getMatchingInputs();

    boolean isEmpty();
}
