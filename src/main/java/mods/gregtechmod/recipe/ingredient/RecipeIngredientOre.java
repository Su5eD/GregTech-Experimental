package mods.gregtechmod.recipe.ingredient;

import com.google.common.base.MoreObjects;
import mods.gregtechmod.core.GregTechMod;

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
                GregTechMod.logger.error("Found empty string among ores: "+ores);
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
        return MoreObjects.toStringHelper(this)
                .add("ingredient", ingredient)
                .add("count", count)
                .toString();
    }
}
