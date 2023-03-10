package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import one.util.streamex.StreamEx;

import java.util.List;

public class ListRecipeIngredientImpl<U> implements ListRecipeIngredient<U> {
    private final ListRecipeIngredientType<?, U> ingredientType;
    private final List<? extends RecipeIngredient<U>> ingredients;

    public ListRecipeIngredientImpl(ListRecipeIngredientType<?, U> ingredientType, List<? extends RecipeIngredient<U>> ingredients) {
        this.ingredientType = ingredientType;
        this.ingredients = ingredients;
    }

    @Override
    public RecipeIngredientType<?, List<U>> getType() {
        return this.ingredientType;
    }

    @Override
    public int getCount() {
        return StreamEx.of(this.ingredients).mapToInt(RecipeIngredient::getCount).sum();
    }

    @Override
    public boolean isEmpty() {
        return StreamEx.of(this.ingredients).allMatch(RecipeIngredient::isEmpty);
    }

    @Override
    public Ingredient asIngredient() {
        return Ingredient.fromValues(StreamEx.of(this.ingredients)
            .map(RecipeIngredient::asIngredient)
            .flatMap(i -> StreamEx.of(i.values)));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.ingredients.forEach(i -> i.toNetwork(buffer));
    }

    @Override
    public JsonElement toJson() {
        JsonArray array = new JsonArray();
        for (RecipeIngredient<U> ingredient : this.ingredients) {
            array.add(ingredient.toJson());
        }
        return array;
    }

    @Override
    public void validate(ResourceLocation id, String name) {
        if (this.ingredients.size() > this.ingredientType.getIngredientCount()) {
            throw new RuntimeException(name + " exceeded max size of " + this.ingredientType.getIngredientCount() + " for recipe " + id);
        }
        for (RecipeIngredient<U> ingredient : this.ingredients) {
            ingredient.validate(id, name);
        }
    }

    @Override
    public boolean test(List<U> list) {
        if (this.ingredients.size() == list.size()) {
            for (int i = 0; i < this.ingredients.size(); i++) {
                if (!this.ingredients.get(i).test(list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public RecipeIngredient<U> get(int index) {
        return this.ingredients.get(index);
    }
}
