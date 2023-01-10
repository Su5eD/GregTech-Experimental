package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class ModRecipeBuilder {
    
    protected abstract ModFinishedRecipe getFinishedRecipe(ResourceLocation recipeId);

    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        ModFinishedRecipe finishedRecipe = getFinishedRecipe(recipeId);
        finishedRecipeConsumer.accept(finishedRecipe);
    }
    
    protected static abstract class ModFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final RecipeSerializer<?> serializer;

        public ModFinishedRecipe(ResourceLocation recipeId, RecipeSerializer<?> serializer) {
            this.recipeId = recipeId;
            this.serializer = serializer;
        }

        @Override
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.serializer;
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
