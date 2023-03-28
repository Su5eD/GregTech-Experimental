package dev.su5ed.gtexperimental.compat.jei;

import dev.su5ed.gtexperimental.compat.ModHandler;
import ic2.integration.jei.recipe.machine.IORecipeWrapper;
import mezz.jei.api.recipe.RecipeType;

import java.util.Map;

public class JEISubmoduleIC2 implements JEIModule.SubModule {
    private static final RecipeType<IORecipeWrapper> MACERATOR = RecipeType.create(ModHandler.IC2_MODID, JEIModule.RECIPE_MACERATOR, IORecipeWrapper.class);
    
    private static final Map<String, RecipeType<?>> RECIPE_TYPES = Map.of(JEIModule.RECIPE_MACERATOR, MACERATOR);
    
    @Override
    public RecipeType<?> getRecipeType(String name) {
        RecipeType<?> recipeType = RECIPE_TYPES.get(name);
        if (recipeType != null) {
            return recipeType;
        }
        throw new IllegalArgumentException("Unknown recipe type " + name);
    }
}
