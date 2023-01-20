package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.recipe.type.FluidRecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredientType;

public final class ModRecipeIngredientTypes {
    public static final VanillaRecipeIngredientType ITEM = new VanillaRecipeIngredientType();
    public static final FluidRecipeIngredientType FLUID = new FluidRecipeIngredientType();

    private ModRecipeIngredientTypes() {}
}
