package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
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
    public boolean apply(@Nullable ItemStack input, boolean checkSize) {
        if (input != null && !this.isEmpty()) {
            for (ItemStack stack : this.getMatchingInputs()) {
                if (GtUtil.stackEquals(stack, input)) {
                    if (checkSize) return input.getCount() >= this.count;
                    else return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<ItemStack> getMatchingInputs() {
        return Arrays.asList(this.ingredient.getMatchingStacks());
    }

    @Override
    public boolean apply(IRecipeIngredient ingredient) {
        return ingredient.getMatchingInputs().stream()
                .anyMatch(stack -> this.apply(stack, false)) && ingredient.getCount() >= this.count;
    }
}
