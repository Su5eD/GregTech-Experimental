package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

/**
 * ItemStack output variant of {@link net.minecraft.data.recipes.SimpleCookingRecipeBuilder}
 */
public class SmeltingRecipeBuilder extends BaseRecipeBuilder {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public SmeltingRecipeBuilder(Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    public SmeltingRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return RecipeSerializer.SMELTING_RECIPE;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        
        json.add("ingredient", this.ingredient.toJson());
        json.add("result", ModRecipeOutputTypes.ITEM.toJson(this.result));
        json.addProperty("experience", this.experience);
        json.addProperty("cookingtime", this.cookingTime);
    }

    @Override
    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId, boolean universal) {
        ensureValid(recipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new SmeltingResult(recipeId));
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return this.advancement.serializeToJson();
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public class SmeltingResult extends BaseFinishedRecipe {
        private final ResourceLocation advancementId;

        public SmeltingResult(ResourceLocation id) {
            super(id);
            this.advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + SmeltingRecipeBuilder.this.result.getItem().getItemCategory().getRecipeFolderName() + "/" + id.getPath());
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
