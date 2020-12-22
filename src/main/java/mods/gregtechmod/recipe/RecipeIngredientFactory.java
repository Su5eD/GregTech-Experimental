package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.IRecipeIngredient;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import net.minecraft.item.ItemStack;

public class RecipeIngredientFactory implements IRecipeIngredientFactory {
    @Override
    public IRecipeIngredient fromStacks(int count, ItemStack... stacks) {
        return RecipeIngredientItemStack.create(count, stacks);
    }

    @Override
    public IRecipeIngredient fromOre(String ore, int count) {
        return RecipeIngredientOre.create(ore, count);
    }
}
