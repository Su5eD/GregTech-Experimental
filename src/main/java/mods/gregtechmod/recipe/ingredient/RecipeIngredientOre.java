package mods.gregtechmod.recipe.ingredient;

import com.google.common.base.MoreObjects;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeIngredientOre extends RecipeIngredient<MultiOreIngredient> {
    public static final RecipeIngredientOre EMPTY = new RecipeIngredientOre(Collections.emptyList(), 0, ItemStack.EMPTY);

    private RecipeIngredientOre(List<String> ores, int count, ItemStack filter) {
        super(new MultiOreIngredient(ores, filter), count);
    }

    public static RecipeIngredientOre create(String ore) {
        return create(ore, 1);
    }

    public static RecipeIngredientOre create(String ore, int count) {
        return ore.isEmpty() ? EMPTY : create(Collections.singletonList(ore), count);
    }

    public static RecipeIngredientOre create(List<String> ores, int count) {
        return create(ores, count, ItemStack.EMPTY);
    }
    
    public static RecipeIngredientOre create(List<String> ores, int count, ItemStack filter) {
        if (ores.stream().anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("Found empty string among ores: " + ores);
        }
        return new RecipeIngredientOre(ores, count, filter);
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
