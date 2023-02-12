package dev.su5ed.gtexperimental.recipe.crafting;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class WrappedShapedRecipeBuilder extends ShapedRecipeBuilder {
    private final RecipeSerializer<?> type;

    public WrappedShapedRecipeBuilder(ItemLike result, int count, RecipeSerializer<?> type) {
        super(result, count);
        this.type = type;
    }

    public static WrappedShapedRecipeBuilder toolShaped(ItemLike result) {
        return toolShaped(result, 1);
    }

    public static WrappedShapedRecipeBuilder toolShaped(ItemLike result, int count) {
        return new WrappedShapedRecipeBuilder(result, count, ModRecipeSerializers.TOOL_SHAPED_RECIPE.get());
    }

    public static WrappedShapedRecipeBuilder fluidShaped(ItemLike result) {
        return fluidShaped(result, 1);
    }

    public static WrappedShapedRecipeBuilder fluidShaped(ItemLike result, int count) {
        return new WrappedShapedRecipeBuilder(result, count, ModRecipeSerializers.FLUID_SHAPED_RECIPE.get());
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        super.save(finishedRecipe -> finishedRecipeConsumer.accept(new FinishedRecipeWrapper(finishedRecipe, this.type)), recipeId);
    }
}
