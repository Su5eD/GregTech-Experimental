package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.GregTechAPI;

import java.util.Collections;
import java.util.List;

public class RecipeIngredientOre extends RecipeIngredient<GtOreIngredient> {
    public static final RecipeIngredientOre EMPTY = new RecipeIngredientOre(Collections.emptyList(), 0);

    private RecipeIngredientOre(List<String> ores, int count) {
        super(new GtOreIngredient(ores), count);
    }

    public static RecipeIngredientOre create(String ore) {
        return create(ore, 1);
    }

    public static RecipeIngredientOre create(String ore, int count) {
        if (ore.isEmpty()) return EMPTY;
        return new RecipeIngredientOre(Collections.singletonList(ore), count);
    }

    public static RecipeIngredientOre create(List<String> ores, int count) {
        for (String ore : ores) {
            if (ore.isEmpty()) {
                GregTechAPI.logger.error("Found empty string among ores: "+ores);
                return EMPTY;
            }
        }

        return new RecipeIngredientOre(ores, count);
    }

    @Override
    public boolean isEmpty() {
        return this.ingredient.getOres().isEmpty();
    }

    @Override
    public String toString() {
        return "RecipeIngredientOre{ores="+this.ingredient.getOres()+",count="+this.count+"}";
    }
}
