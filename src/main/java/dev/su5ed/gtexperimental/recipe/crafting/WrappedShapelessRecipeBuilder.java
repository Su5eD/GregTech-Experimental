package dev.su5ed.gtexperimental.recipe.crafting;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class WrappedShapelessRecipeBuilder extends ShapelessRecipeBuilder {
    private final RecipeSerializer<?> type;

    public WrappedShapelessRecipeBuilder(ItemLike result, int count, RecipeSerializer<?> type) {
        super(result, count);
        this.type = type;
    }

    public static WrappedShapelessRecipeBuilder toolShapeless(ItemLike result) {
        return toolShapeless(result, 1);
    }

    public static WrappedShapelessRecipeBuilder toolShapeless(ItemLike result, int count) {
        return new WrappedShapelessRecipeBuilder(result, count, ModRecipeSerializers.TOOL_SHAPELESS_RECIPE.get());
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        super.save(finishedRecipe -> finishedRecipeConsumer.accept(new FinishedRecipeWrapper(finishedRecipe, this.type)), recipeId);
    }
}
