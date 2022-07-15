package dev.su5ed.gregtechmod.api;

import dev.su5ed.gregtechmod.api.cover.ICoverProvider;
import dev.su5ed.gregtechmod.api.recipe.IRecipeFactory;
import dev.su5ed.gregtechmod.api.recipe.IRecipeIngredientFactory;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class GregTechAPI {
    public static Supplier<IForgeRegistry<ICoverProvider>> coverRegistry;
    private static IGregTechAPI impl;
    private static IRecipeFactory recipeFactory;
    private static IRecipeIngredientFactory ingredientFactory;

    private GregTechAPI() {}

    public static IGregTechAPI instance() {
        return impl;
    }

    public static IRecipeFactory getRecipeFactory() {
        return recipeFactory;
    }

    public static IRecipeIngredientFactory getIngredientFactory() {
        return ingredientFactory;
    }
}
