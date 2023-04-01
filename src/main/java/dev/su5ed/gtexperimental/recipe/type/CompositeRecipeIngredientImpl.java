package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import one.util.streamex.StreamEx;

import java.util.List;

public class CompositeRecipeIngredientImpl<T> implements CompositeRecipeIngredient<T> {
    private final RecipeIngredientType<?, T> type;
    private final List<SubIngredient<T, ?>> subIngredients;

    public CompositeRecipeIngredientImpl(RecipeIngredientType<?, T> type, List<SubIngredient<T, ?>> subIngredients) {
        this.type = type;
        this.subIngredients = subIngredients;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> RecipeIngredient<U> getSubIngredient(String name) {
        for (SubIngredient<T, ?> subIngredient : this.subIngredients) {
            if (subIngredient.name().equals(name)) {
                return (RecipeIngredient<U>) subIngredient.ingredient();
            }
        }
        throw new IllegalArgumentException("Unknown sub-ingredient " + name);
    }

    @Override
    public RecipeIngredientType<?, T> getType() {
        return this.type;
    }

    @Override
    public int getCount() {
        int total = 0;
        for (SubIngredient<T, ?> subIngredient : this.subIngredients) {
            total += subIngredient.ingredient().getCount();
        }
        return total;
    }

    @Override
    public boolean isEmpty() {
        for (SubIngredient<T, ?> subIngredient : this.subIngredients) {
            if (!subIngredient.ingredient().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Ingredient asIngredient() {
        return StreamEx.of(this.subIngredients)
            .flatArray(subIngredient -> subIngredient.ingredient().asIngredient().values)
            .chain(Ingredient::fromValues);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeCollection(this.subIngredients, (buf, subIngredient) -> {
            buf.writeUtf(subIngredient.name());
            subIngredient.ingredient().toNetwork(buf);
        });
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        for (SubIngredient<T, ?> subIngredient : this.subIngredients) {
            json.add(subIngredient.name(), subIngredient.ingredient().toJson());
        }
        return json;
    }

    @Override
    public void validate(ResourceLocation id, String name) {
        for (SubIngredient<T, ?> subIngredient : this.subIngredients) {
            subIngredient.ingredient().validate(id, name + " / " + subIngredient.name());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean testPartial(T value) {
        for (SubIngredient<T, ?> subIngredient : this.subIngredients) {
            Object ingredientValue = subIngredient.valueFunction().apply(value);
            if (((RecipeIngredient<Object>) subIngredient.ingredient()).testPartial(ingredientValue)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean test(T value) {
        for (SubIngredient<T, ?> subIngredient : this.subIngredients) {
            Object ingredientValue = subIngredient.valueFunction().apply(value);
            if (!((RecipeIngredient<Object>) subIngredient.ingredient()).test(ingredientValue)) {
                return false;
            }
        }
        return true;
    }
}
