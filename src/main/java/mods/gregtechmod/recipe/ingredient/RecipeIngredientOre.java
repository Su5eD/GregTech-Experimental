package mods.gregtechmod.recipe.ingredient;

import com.google.common.base.MoreObjects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class RecipeIngredientOre extends RecipeIngredient<MultiOreIngredient> {
    public static final RecipeIngredientOre EMPTY = new RecipeIngredientOre(Collections.emptyList(), 0);
    private static final Logger LOGGER = LogManager.getLogger();

    private RecipeIngredientOre(List<String> ores, int count) {
        super(new MultiOreIngredient(ores), count);
    }

    public static RecipeIngredientOre create(String ore) {
        return create(ore, 1);
    }

    public static RecipeIngredientOre create(String ore, int count) {
        return ore.isEmpty() ? EMPTY : new RecipeIngredientOre(Collections.singletonList(ore), count);
    }

    public static RecipeIngredientOre create(List<String> ores, int count) {
        if (ores.stream().anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("Found empty string among ores: " + ores);
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
