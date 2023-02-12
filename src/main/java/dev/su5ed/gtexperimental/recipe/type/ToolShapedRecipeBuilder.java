package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ToolShapedRecipeBuilder extends ShapedRecipeBuilder {

    public ToolShapedRecipeBuilder(ItemLike presult, int count) {
        super(presult, count);
    }

    public static ToolShapedRecipeBuilder toolShaped(ItemLike result) {
        return toolShaped(result, 1);
    }

    public static ToolShapedRecipeBuilder toolShaped(ItemLike result, int count) {
        return new ToolShapedRecipeBuilder(result, count);
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        super.save(finishedRecipe -> finishedRecipeConsumer.accept(new ToolShapedRecipeBuilder.Result(finishedRecipe)), recipeId);
    }

    public static class Result implements FinishedRecipe {
        private final FinishedRecipe result;

        public Result(FinishedRecipe result) {
            this.result = result;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipeSerializers.TOOL_SHAPED_RECIPE.get();
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
}
