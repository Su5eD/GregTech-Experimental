package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.IRecipeIngredient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Collection;

public class RecipeIngredientItemStack extends Ingredient implements IRecipeIngredient {
    private final int count;

    private RecipeIngredientItemStack(int count, ItemStack... stacks) {
        super(stacks);
        this.count = count;
    }

    public static RecipeIngredientItemStack create(Item item) {
        return create(item, 1);
    }

    public static RecipeIngredientItemStack create(Item item, int count) {
        return create(item, count, 0);
    }

    public static RecipeIngredientItemStack create(Item item, int count, int meta) {
        return create(new ItemStack(item, count, meta));
    }

    public static RecipeIngredientItemStack create(ItemStack stack) {
        if (stack.isEmpty()) return null;
        return create(stack.getCount(), stack);
    }

    public static RecipeIngredientItemStack create(int count, ItemStack... stacks) {
        if (stacks.length < 1) return null;
        return new RecipeIngredientItemStack(count, stacks);
    }

    @Override
    public Ingredient asIngredient() {
        return this;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Collection<ItemStack> getMatchingInputs() {
        return Arrays.asList(super.getMatchingStacks());
    }
}
