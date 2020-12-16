package mods.gregtechmod.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Collection;

public interface IRecipeIngredient {
    Ingredient asIngredient();

    int getCount();

    boolean apply(ItemStack stack);

    Collection<ItemStack> getMatchingInputs();
}
