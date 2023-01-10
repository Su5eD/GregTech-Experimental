package dev.su5ed.gregtechmod.recipe.setup;

import dev.su5ed.gregtechmod.recipe.type.FluidRecipeIngredient;
import dev.su5ed.gregtechmod.recipe.type.FluidRecipeIngredientType;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredientType;
import dev.su5ed.gregtechmod.recipe.type.VanillaRecipeIngredient;
import dev.su5ed.gregtechmod.recipe.type.VanillaRecipeIngredientType;

public final class ModRecipeIngredientTypes {
    public static final RecipeIngredientType<VanillaRecipeIngredient> ITEM = new VanillaRecipeIngredientType();
    public static final RecipeIngredientType<FluidRecipeIngredient> FLUID = new FluidRecipeIngredientType();

    private ModRecipeIngredientTypes() {}
}
