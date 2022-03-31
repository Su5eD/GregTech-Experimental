package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class RecipeIngredientDamagedStack extends RecipeIngredientItemStack {

    private RecipeIngredientDamagedStack(int count, ItemStack... stacks) {
        super(count, stacks);
    }

    public static RecipeIngredientItemStack create(ItemStack stack, int count) {
        return stack.isEmpty() ? EMPTY : new RecipeIngredientDamagedStack(count, stack);
    }

    @Override
    public boolean apply(ItemStack input, boolean checkSize) {
        return super.apply(input, checkSize) && input.isItemDamaged();
    }

    @Override
    public boolean apply(IRecipeIngredient ingredient) {
        return ingredient.stream()
            .anyMatch(stack -> super.apply(stack, false)) && ingredient.getCount() >= this.count;
    }
}
