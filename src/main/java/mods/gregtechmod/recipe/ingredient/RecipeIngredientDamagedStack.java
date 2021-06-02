package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class RecipeIngredientDamagedStack extends RecipeIngredientItemStack {

    private RecipeIngredientDamagedStack(int count, ItemStack... stacks) {
        super(count, stacks);
    }
    
    public static RecipeIngredientItemStack create(ItemStack stack, int count) {
        if (stack.isEmpty()) return EMPTY;
        return new RecipeIngredientDamagedStack(count, stack);
    }

    @Override
    public boolean apply(@Nullable ItemStack input, boolean checkSize) {
        return super.apply(input, checkSize) && input.isItemDamaged();
    }

    @Override
    public boolean apply(IRecipeIngredient ingredient) {
        return ingredient.getMatchingInputs().stream()
                .anyMatch(stack -> super.apply(stack, false)) && ingredient.getCount() >= this.count;
    }
}
