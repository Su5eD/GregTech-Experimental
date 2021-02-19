package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class RecipeManagerFusion<I extends IRecipeIngredient, T> extends RecipeManager<List<I>, List<ItemStack>, IRecipeFusion<I, T>> implements IGtRecipeManagerBasic<List<I>, List<ItemStack>, IRecipeFusion<I, T>> {

    public RecipeManagerFusion() {
        super(new FusionRecipeComparator<>());
    }

    @Override
    public boolean hasRecipeFor(List<ItemStack> input) {
        return this.recipes.stream()
                .anyMatch(recipe -> recipe.getInput().stream()
                        .allMatch(ingredient -> input.stream()
                                .anyMatch(ingredient::apply)));
    }

    @Override
    public IRecipeFusion<I, T> getRecipeFor(List<ItemStack> input) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().stream()
                        .allMatch(ingredient -> input.stream()
                                .anyMatch(ingredient::apply)))
                .findFirst()
                .orElse(null);
    }

    private static class FusionRecipeComparator<I extends IRecipeIngredient, T> implements Comparator<IRecipeFusion<I, T>> {

        @Override
        public int compare(IRecipeFusion<I, T> first, IRecipeFusion<I, T> second) {
            return first.getInput().stream()
                    .flatMapToInt(firstInput -> second.getInput().stream()
                            .mapToInt(firstInput::compareTo))
                    .sum();
        }
    }
}
