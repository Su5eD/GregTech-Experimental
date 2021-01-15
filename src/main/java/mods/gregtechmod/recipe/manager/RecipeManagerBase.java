package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public abstract class RecipeManagerBase<R extends IGtMachineRecipe<IRecipeIngredient, ?>> extends RecipeManager<IRecipeIngredient, ItemStack, R> {

    public RecipeManagerBase(Comparator<R> comparator) {
        super(comparator);
    }

    @Override
    public boolean hasRecipeFor(ItemStack input) {
        return this.recipes.stream()
                .anyMatch(recipe -> recipe.getInput().apply(input, false));
    }
}
