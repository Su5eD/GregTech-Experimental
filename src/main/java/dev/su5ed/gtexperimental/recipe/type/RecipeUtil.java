package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;

public final class RecipeUtil {

    public static void validateInput(ResourceLocation id, String name, RecipeIngredient<?> ingredient) {
        if (ingredient.isEmpty()) {
            throw new RuntimeException("Empty " + name + " ingredient in recipe " + id);
        }
    }

    public static void validateInputList(ResourceLocation id, String name, List<? extends RecipeIngredient<?>> ingredients, int maxSize) {
        if (ingredients.isEmpty()) {
            throw new RuntimeException("Empty " + name + " for recipe " + id);
        }
        else if (ingredients.size() > maxSize) {
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
        if (items.isEmpty()) {
            throw new RuntimeException("Empty " + name + " for recipe " + id);
        }
        else if (items.size() > maxSize) {
            throw new RuntimeException(name + " exceeded max size of " + maxSize + " for recipe " + id);
        }
        else if (StreamEx.of(items).allMatch(ItemStack::isEmpty)) {
            throw new RuntimeException(name + " contained no items for recipe " + id);
        }
    }

    public static <R extends BaseRecipe<?, ?, ? super R>> int compareCount(R first, R second) {
        return second.compareInputCount(first);
    }

    public static JsonArray serializeConditions(Collection<ICondition> conditions) {
        JsonArray jsonConditions = new JsonArray();
        for (ICondition condition : conditions) {
            jsonConditions.add(CraftingHelper.serialize(condition));
        }
        return jsonConditions;
    }

    private RecipeUtil() {}
}
