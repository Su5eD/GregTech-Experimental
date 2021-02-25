package mods.gregtechmod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;

@JEIPlugin
public class JEIModule implements IModPlugin {
    public static IIngredientRegistry itemRegistry;
    public static IIngredientBlacklist ingredientBlacklist;

    @Override
    public void register(IModRegistry registry) {
        itemRegistry = registry.getIngredientRegistry();
        ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
    }
}