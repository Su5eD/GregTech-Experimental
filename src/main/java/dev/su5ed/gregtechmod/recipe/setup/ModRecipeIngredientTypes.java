package dev.su5ed.gregtechmod.recipe.setup;

import dev.su5ed.gregtechmod.recipe.type.FluidRecipeIngredientType;
import dev.su5ed.gregtechmod.recipe.type.VanillaRecipeIngredientType;

public final class ModRecipeIngredientTypes {
    public static final VanillaRecipeIngredientType ITEM = new VanillaRecipeIngredientType();
    public static final FluidRecipeIngredientType FLUID = new FluidRecipeIngredientType();

    private ModRecipeIngredientTypes() {}
}
