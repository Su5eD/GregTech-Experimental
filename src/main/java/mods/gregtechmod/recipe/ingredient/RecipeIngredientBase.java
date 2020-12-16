package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Collection;

public abstract class RecipeIngredientBase<T extends Ingredient> implements IRecipeIngredient {
    protected final T ingredient;
    protected final int count;

    protected RecipeIngredientBase(T ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public Ingredient asIngredient() {
        return this.ingredient;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean apply(ItemStack stack) {
        return this.ingredient.apply(stack) && stack.getCount() >= this.count;
    }

    @Override
    public Collection<ItemStack> getMatchingInputs() {
        return Arrays.asList(this.ingredient.getMatchingStacks());
    }
}
