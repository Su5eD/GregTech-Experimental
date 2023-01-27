package dev.su5ed.gtexperimental.recipe.setup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.type.FluidRecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.type.HybridRecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredientType;
import net.minecraft.network.FriendlyByteBuf;
import one.util.streamex.StreamEx;

import java.util.List;

public final class ModRecipeIngredientTypes {
    public static final VanillaRecipeIngredientType ITEM = new VanillaRecipeIngredientType();
    public static final FluidRecipeIngredientType FLUID = new FluidRecipeIngredientType();
    public static final HybridRecipeIngredientType HYBRID = new HybridRecipeIngredientType();

    public static <T> void toNetwork(List<? extends RecipeIngredient<T>> ingredients, FriendlyByteBuf buffer) {
        buffer.writeInt(ingredients.size());
        for (RecipeIngredient<T> ingredient : ingredients) {
            ingredient.toNetwork(buffer);
        }
    }

    public static <T> List<? extends RecipeIngredient<T>> fromNetwork(RecipeIngredientType<? extends RecipeIngredient<T>> ingredientType, int inputCount, FriendlyByteBuf buffer) {
        int ingredientCount = buffer.readInt();
        if (inputCount >= ingredientCount) {
            return StreamEx.generate(() -> ingredientType.create(buffer))
                .limit(ingredientCount)
                .toList();
        }
        throw new IllegalArgumentException("There are more ingredients than known ingredient types");
    }
    
    public static <T> JsonElement toJson(List<? extends RecipeIngredient<T>> ingredients) {
        JsonArray json = new JsonArray(ingredients.size());
        for (RecipeIngredient<T> input : ingredients) {
            json.add(input.toJson());
        }
        return json;
    }

    private ModRecipeIngredientTypes() {}
}
