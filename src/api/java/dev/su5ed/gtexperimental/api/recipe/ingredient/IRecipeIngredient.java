package dev.su5ed.gtexperimental.api.recipe.ingredient;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import one.util.streamex.StreamEx;

import java.util.List;

public interface IRecipeIngredient {
    Ingredient asIngredient();

    int getCount();

    default boolean apply(ItemStack stack) {
        return apply(stack, true);
    }

    boolean apply(ItemStack stack, boolean checkCount);

    boolean apply(IRecipeIngredient ingredient);
    
    StreamEx<ItemStack> stream();

    List<ItemStack> getMatchingInputs();

    boolean isEmpty();
}
