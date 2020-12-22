package mods.gregtechmod.recipe.ingredient;

import net.minecraftforge.oredict.OreIngredient;

public class RecipeIngredientOre extends RecipeIngredientBase<OreIngredient> {

    private RecipeIngredientOre(String ore, int count) {
        super(new OreIngredient(ore), count);
    }

    public static RecipeIngredientOre create(String ore) {
        return create(ore, 1);
    }

    public static RecipeIngredientOre create(String ore, int count) {
        if (ore.isEmpty()) return null;
        return new RecipeIngredientOre(ore, count);
    }
}
