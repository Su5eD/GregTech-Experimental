package mods.gregtechmod.api.recipe.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public interface IRecipeIngredient extends Comparable<IRecipeIngredient> {
    Ingredient asIngredient();

    int getCount();

    boolean apply(ItemStack stack);

    List<ItemStack> getMatchingInputs();
}
