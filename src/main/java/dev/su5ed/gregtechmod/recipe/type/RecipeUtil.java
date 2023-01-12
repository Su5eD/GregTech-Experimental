package dev.su5ed.gregtechmod.recipe.type;

public final class RecipeUtil {

    public static <R extends BaseRecipe<?, ?, ? super R>> int compareCount(R first, R second) {
        return second.compareInputCount(first);
    }

    private RecipeUtil() {}
}
