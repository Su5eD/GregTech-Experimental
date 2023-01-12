package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class ModRecipeBuilder<R extends BaseRecipe<?, ?, ? super R>> {
    protected final R recipe;

    public ModRecipeBuilder(R recipe) {
        this.recipe = recipe;
    }

    public abstract void serializeRecipeData(JsonObject json);

    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        ModFinishedRecipe finishedRecipe = new ModFinishedRecipe(recipeId);
        finishedRecipeConsumer.accept(finishedRecipe);
    }
    
    protected class ModFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation recipeId;

        public ModFinishedRecipe(ResourceLocation recipeId) {
            this.recipeId = recipeId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            ModRecipeBuilder.this.serializeRecipeData(json);
        }

        @Override
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipeBuilder.this.recipe.getSerializer();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
