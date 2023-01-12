package dev.su5ed.gregtechmod.recipe.type;

public interface RecipeHolder<R extends BaseRecipe<?, I, ? super R>, I> {
    /**
     * Checks if a recipe exists for the target input without checking the stacksize
     */
    boolean hasRecipeFor(I input);
}
