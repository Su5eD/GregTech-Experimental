package mods.gregtechmod.recipe.ingredient;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.util.ItemStackComparator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

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
    public List<ItemStack> getMatchingInputs() {
        return Arrays.asList(this.ingredient.getMatchingStacks());
    }

    @Override
    public int compareTo(IRecipeIngredient other) {
        int diff = 0;
        List<ItemStack> matchingInputs = this.getMatchingInputs();
        List<ItemStack> otherMatchingInputs = other.getMatchingInputs();

        if (otherMatchingInputs.isEmpty()) return -other.compareTo(this);

        for (ItemStack firstStack : matchingInputs) {
            for (ItemStack secondStack : otherMatchingInputs) {
                diff += ItemStackComparator.INSTANCE.compare(StackUtil.setSize(firstStack, this.count), StackUtil.setSize(secondStack, other.getCount()));
            }
        }

        return diff;
    }
}
