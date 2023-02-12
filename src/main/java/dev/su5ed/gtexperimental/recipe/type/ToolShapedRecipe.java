package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class ToolShapedRecipe extends ShapedRecipe {

    public ToolShapedRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) {
        super(id, group, width, height, recipeItems, result);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        outer:
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = container.getItem(i);
            for (Ingredient ingredient : getIngredients()) {
                if (ingredient instanceof ToolCraftingIngredient tool && tool.test(stack)) {
                    list.set(i, tool.damageItem(stack).copy());
                    continue outer;
                }
            }
            if (stack.hasCraftingRemainingItem()) {
                list.set(i, stack.getCraftingRemainingItem());
            }
        }
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.TOOL_SHAPED_RECIPE.get();
    }

    public static class Serializer extends ShapedRecipe.Serializer {
        @Override
        public ToolShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = super.fromJson(recipeId, json);
            return new ToolShapedRecipe(recipeId, recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Override
        public ToolShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapedRecipe recipe = super.fromNetwork(recipeId, buffer);
            return new ToolShapedRecipe(recipeId, recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem());
        }
    }
}
