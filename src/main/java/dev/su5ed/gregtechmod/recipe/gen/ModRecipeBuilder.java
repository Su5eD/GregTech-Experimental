package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ModRecipeBuilder<R extends BaseRecipe<?, ?, ? super R>> {
    protected final R recipe;
    private final List<ICondition> conditions;

    public ModRecipeBuilder(R recipe) {
        this.recipe = recipe;
        this.conditions = new ArrayList<>();
    }

    public abstract void serializeRecipeData(JsonObject json);

    public ModRecipeBuilder<R> addCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

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
            if (!ModRecipeBuilder.this.conditions.isEmpty()) {
                JsonArray jsonConditions = new JsonArray();
                for (ICondition condition : ModRecipeBuilder.this.conditions) {
                    jsonConditions.add(CraftingHelper.serialize(condition));
                }
                json.add("conditions", jsonConditions);
            }
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
