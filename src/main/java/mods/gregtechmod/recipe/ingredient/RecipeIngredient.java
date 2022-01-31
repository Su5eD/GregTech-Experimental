package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.List;

public abstract class RecipeIngredient<T extends Ingredient> implements IRecipeIngredient {
    protected final T ingredient;
    protected final int count;

    protected RecipeIngredient(T ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public T asIngredient() {
        return this.ingredient;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean apply(ItemStack input, boolean checkSize) {
        return !input.isEmpty() && !isEmpty() && stream()
            .filter(stack -> GtUtil.stackEquals(stack, input))
            .anyMatch(stack -> !checkSize || input.getCount() >= this.count);
    }

    @Override
    public StreamEx<ItemStack> stream() {
        return StreamEx.of(this.ingredient.getMatchingStacks());
    }

    @Override
    public List<ItemStack> getMatchingInputs() {
        return Arrays.asList(this.ingredient.getMatchingStacks());
    }

    @Override
    public boolean apply(IRecipeIngredient ingredient) {
        return ingredient.stream()
            .anyMatch(stack -> apply(stack, false)) && ingredient.getCount() >= this.count;
    }
}
