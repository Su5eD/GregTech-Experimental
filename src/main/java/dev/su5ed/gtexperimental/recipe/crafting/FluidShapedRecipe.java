package dev.su5ed.gtexperimental.recipe.crafting;

import dev.su5ed.gtexperimental.recipe.type.VanillaFluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class FluidShapedRecipe extends ShapedRecipe {
    public static final RecipeSerializer<ShapedRecipe> SERIALIZER = new SimpleRecipeSerializer<>(RecipeSerializer.SHAPED_RECIPE, FluidShapedRecipe::new);

    public FluidShapedRecipe(ResourceLocation id, ShapedRecipe recipe) {
        this(id, recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem());
    }

    public FluidShapedRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) {
        super(id, group, width, height, recipeItems, result);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        outer:
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = container.getItem(i);
            for (Ingredient ingredient : getIngredients()) {
                if (ingredient instanceof VanillaFluidIngredient fluid && fluid.test(stack)) {
                    fluid.drainFluid(stack);
                    list.set(i, stack.copy());
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
        return SERIALIZER;
    }
}
