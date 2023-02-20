package dev.su5ed.gtexperimental.recipe.crafting;

import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ToolShapelessRecipe extends ShapelessRecipe {
    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new SimpleRecipeSerializer<>(RecipeSerializer.SHAPELESS_RECIPE, ToolShapelessRecipe::new);

    public ToolShapelessRecipe(ResourceLocation id, ShapelessRecipe recipe) {
        this(id, recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
    }

    public ToolShapelessRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, result, ingredients);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        return RecipeUtil.getRemainingToolItems(getIngredients(), container);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
