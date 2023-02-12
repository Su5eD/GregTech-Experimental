package dev.su5ed.gtexperimental.recipe.crafting;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

public class FinishedRecipeWrapper implements FinishedRecipe {
    private final FinishedRecipe result;
    private final RecipeSerializer<?> type;

    public FinishedRecipeWrapper(FinishedRecipe result, RecipeSerializer<?> type) {
        this.result = result;
        this.type = type;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.type;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        this.result.serializeRecipeData(json);
    }

    @Override
    public ResourceLocation getId() {
        return this.result.getId();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return this.result.serializeAdvancement();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return this.result.getAdvancementId();
    }
}
