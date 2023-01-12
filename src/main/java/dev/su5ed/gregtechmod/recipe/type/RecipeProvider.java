package dev.su5ed.gregtechmod.recipe.type;

public interface RecipeProvider<R extends BaseRecipe<?, I, ? super R>, I> extends RecipeHolder<R, I> {
    /**
     * Checks if a recipe exists which's input exactly matches the target input provided
     */
    R getRecipeFor(I input);
}
