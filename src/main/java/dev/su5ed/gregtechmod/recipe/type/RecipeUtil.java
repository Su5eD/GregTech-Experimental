package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public final class RecipeUtil {

    public static void validateInput(ResourceLocation id, String name, RecipeIngredient<?> ingredient) {
        if (ingredient.isEmpty()) {
            throw new RuntimeException("Empty " + name + " ingredient in recipe " + id);
        }
    }

    public static void validateInputList(ResourceLocation id, String name, List<? extends RecipeIngredient<?>> ingredients, int maxSize) {
        if (ingredients.size() > maxSize) {
            throw new RuntimeException(name + " exceeded max size of " + maxSize + " for recipe " + id);
        }
        else if (StreamEx.of(ingredients).allMatch(RecipeIngredient::isEmpty)) {
            throw new RuntimeException(name + " contained no ingredients for recipe " + id);
        }
    }

    public static void validateItem(ResourceLocation id, String name, ItemStack item) {
        if (item.isEmpty()) {
            throw new RuntimeException("Empty " + name + " ingredient in recipe " + id);
        }
    }

    public static void validateItemList(ResourceLocation id, String name, List<ItemStack> items, int maxSize) {
        if (items.size() > maxSize) {
            throw new RuntimeException(name + " exceeded max size of " + maxSize + " for recipe " + id);
        }
        else if (StreamEx.of(items).allMatch(ItemStack::isEmpty)) {
            throw new RuntimeException(name + " contained no items for recipe " + id);
        }
    }

    public static <R extends BaseRecipe<?, ?, ? super R>> int compareCount(R first, R second) {
        return second.compareInputCount(first);
    }

    private RecipeUtil() {}
}
