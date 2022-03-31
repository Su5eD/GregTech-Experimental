package mods.gregtechmod.recipe.ingredient;

import com.google.common.base.MoreObjects;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public class RecipeIngredientWrapped<T extends Ingredient> extends RecipeIngredient<T> {
    
    private RecipeIngredientWrapped(T ingredient, int count) {
        super(ingredient, count);
    }
    
    public static <T extends Ingredient> RecipeIngredientWrapped<T> create(T ingredient, int count) {
        return new RecipeIngredientWrapped<>(ingredient, count);
    }

    @Override
    public boolean isEmpty() {
        return !(this.ingredient instanceof OreIngredient) && getMatchingInputs().isEmpty();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("ingredient", ingredient)
            .add("count", count)
            .toString();
    }
}
