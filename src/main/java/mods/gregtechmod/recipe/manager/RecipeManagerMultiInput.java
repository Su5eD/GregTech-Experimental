package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class RecipeManagerMultiInput<R extends IMachineRecipe<List<I>, ?>, I extends IRecipeIngredient> extends RecipeManager<List<I>, List<ItemStack>, R> implements IGtRecipeManagerBasic<List<I>, List<ItemStack>, R> {

    @Override
    public boolean hasRecipeFor(List<ItemStack> input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> input.stream()
                    .anyMatch(stack -> ingredient.apply(stack, false))));
    }

    @Override
    public boolean hasRecipeFor(ItemStack input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().stream()
                .anyMatch(ingredient -> ingredient.apply(input, false)));
    }

    @Override
    protected R getRecipeForExact(R recipe) {
        return StreamEx.of(this.recipes)
            .findFirst(r -> r.getInput().stream()
                .allMatch(ingredient -> recipe.getInput().stream()
                    .anyMatch(ingredient::apply)) && compareCount(r, recipe) == 0)
            .orElse(null);
    }

    @Override
    public R getRecipeFor(List<ItemStack> input) {
        return this.recipes.stream()
            .filter(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> input.stream()
                    .anyMatch(ingredient::apply)))
            .min(this::compareCount)
            .orElseGet(() -> getProvidedRecipe(input));
    }

    public int compareCount(R first, R second) {
        return second.getInput().stream()
            .mapToInt(IRecipeIngredient::getCount)
            .sum() - first.getInput().stream()
            .mapToInt(IRecipeIngredient::getCount)
            .sum();
    }
}
