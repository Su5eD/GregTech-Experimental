package mods.gregtechmod.api;

import mods.gregtechmod.api.cover.ICoverRegistry;
import mods.gregtechmod.api.recipe.IRecipeFactory;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("unused")
public class GregTechAPI {
    private static IGregTechAPI impl;
    private static ICoverRegistry coverRegistry;
    private static Configuration dynamicConfig;
    private static IRecipeFactory recipeFactory;
    private static IRecipeIngredientFactory ingredientFactory;

    public static boolean getDynamicConfig(String category, String name, boolean value) {
        boolean ret = dynamicConfig.get(category, name, value).getBoolean();
        if (dynamicConfig.hasChanged()) dynamicConfig.save();
        return ret;
    }
    
    public static IGregTechAPI instance() {
        return impl;
    }

    public static ICoverRegistry getCoverRegistry() {
        return coverRegistry;
    }

    public static IRecipeFactory getRecipeFactory() {
        return recipeFactory;
    }

    public static IRecipeIngredientFactory getIngredientFactory() {
        return ingredientFactory;
    }
}
