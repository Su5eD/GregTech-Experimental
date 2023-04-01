package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeRecipeIngredientTypeImpl<T> implements CompositeRecipeIngredientType<T> {
    private final Map<String, SubIngredientType<T, ?>> subIngredientTypes;

    public CompositeRecipeIngredientTypeImpl(Map<String, SubIngredientType<T, ?>> subIngredientTypes) {
        this.subIngredientTypes = subIngredientTypes;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CompositeRecipeIngredient<T> createIngredient(Map<String, ? extends RecipeIngredient<?>> subIngredients) {
        List<CompositeRecipeIngredient.SubIngredient<T, ?>> subIngredientList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : subIngredients.entrySet()) {
            String name = entry.getKey();
            SubIngredientType<T, ?> subIngredientType = getSubIngredientType(name);
            subIngredientList.add(subIngredientType.toIngredient(name, (RecipeIngredient) entry.getValue()));
        }
        return new CompositeRecipeIngredientImpl<>(this, subIngredientList);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public CompositeRecipeIngredient<T> create(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        List<CompositeRecipeIngredient.SubIngredient<T, ?>> subIngredients = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String name = entry.getKey();
            SubIngredientType<T, ?> subIngredientType = getSubIngredientType(name);
            RecipeIngredient ingredient = subIngredientType.type().create(entry.getValue());
            subIngredients.add(subIngredientType.toIngredient(name, ingredient));
        }
        return new CompositeRecipeIngredientImpl<>(this, subIngredients);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public CompositeRecipeIngredient<T> create(FriendlyByteBuf buffer) {
        List<CompositeRecipeIngredient.SubIngredient<T, ?>> subIngredients = buffer.readList(buf -> {
            String name = buf.readUtf();
            SubIngredientType<T, ?> subIngredientType = getSubIngredientType(name);
            RecipeIngredient ingredient = subIngredientType.type().create(buf);
            return subIngredientType.toIngredient(name, ingredient);
        });
        return new CompositeRecipeIngredientImpl<>(this, subIngredients);
    }

    @Override
    public ListRecipeIngredientType<List<CompositeRecipeIngredient<T>>, T> listOf(int count) {
        return new ListRecipeIngredientTypeImpl<>(this, count);
    }

    private SubIngredientType<T, ?> getSubIngredientType(String name) {
        SubIngredientType<T, ?> subIngredientType = this.subIngredientTypes.get(name);
        if (subIngredientType != null) {
            return subIngredientType;
        }
        throw new IllegalArgumentException("Unknown sub-ingredient type " + name);
    }
}
