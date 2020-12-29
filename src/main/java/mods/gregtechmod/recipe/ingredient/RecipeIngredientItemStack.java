package mods.gregtechmod.recipe.ingredient;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class RecipeIngredientItemStack extends RecipeIngredientBase<Ingredient> {

    protected RecipeIngredientItemStack(int count, ItemStack... stacks) {
        super(Ingredient.fromStacks(stacks), count);
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
}
