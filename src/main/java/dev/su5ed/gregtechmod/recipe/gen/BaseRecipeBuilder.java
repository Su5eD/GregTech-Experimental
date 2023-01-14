package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.RecipeUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseRecipeBuilder {
    protected final List<ICondition> conditions = new ArrayList<>();

    public BaseRecipeBuilder addConditions(ICondition... conditions) {
        return addConditions(List.of(conditions));
    }

    public BaseRecipeBuilder addConditions(Collection<ICondition> conditions) {
        this.conditions.addAll(conditions);
        return this;
    }

    public abstract RecipeSerializer<?> getType();

    public final JsonObject serializeRecipe() {
        JsonObject json = new JsonObject();
        serializeRecipe(json);
        serializeRecipeData(json);
        return json;
    }

    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", ForgeRegistries.RECIPE_SERIALIZERS.getKey(getType()).toString());
    }

    public void serializeRecipeData(JsonObject json) {
        if (!this.conditions.isEmpty()) {
            json.add("conditions", RecipeUtil.serializeConditions(this.conditions));
        }
    }

    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        build(finishedRecipeConsumer, recipeId, false);
    }

    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId, boolean universal) {
        BaseFinishedRecipe finishedRecipe = new BaseFinishedRecipe(recipeId);
        finishedRecipeConsumer.accept(finishedRecipe);
    }

    @Nullable
    public JsonObject serializeAdvancement() {
        return null;
    }

    protected class BaseFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation recipeId;

        public BaseFinishedRecipe(ResourceLocation recipeId) {
            this.recipeId = recipeId;
        }

        @Override
        public JsonObject serializeRecipe() {
            return BaseRecipeBuilder.this.serializeRecipe();
        }

        @Override
        public void serializeRecipeData(JsonObject json) {}

        @Override
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BaseRecipeBuilder.this.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return BaseRecipeBuilder.this.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
