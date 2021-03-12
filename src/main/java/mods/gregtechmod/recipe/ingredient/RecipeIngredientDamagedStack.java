package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class RecipeIngredientDamagedStack extends RecipeIngredientItemStack {

    private RecipeIngredientDamagedStack(int count, ItemStack... stacks) {
        super(count, stacks);
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
