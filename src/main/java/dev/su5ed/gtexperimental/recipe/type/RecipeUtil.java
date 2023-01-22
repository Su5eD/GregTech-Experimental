package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static dev.su5ed.gtexperimental.api.Reference.location;

public final class RecipeUtil {

    public static <T> List<? extends RecipeIngredient<T>> parseInputs(RecipeIngredientType<? extends RecipeIngredient<T>> inputType, int inputCount, JsonArray json) {
        if (!json.isEmpty() && inputCount >= json.size()) {
            return StreamEx.of(json.getAsJsonArray().iterator())
                .map(inputType::create)
                .toList();
        }
        throw new IllegalArgumentException("Expected " + inputCount + " inputs, got " + json.size());
    }

    public static <T> List<? extends RecipeIngredient<T>> parseInputs(List<? extends RecipeIngredientType<? extends RecipeIngredient<T>>> inputTypes, JsonArray json) {
        if (!json.isEmpty() && inputTypes.size() >= json.size()) {
            return StreamEx.of(inputTypes)
                .zipWith(StreamEx.of(json.getAsJsonArray().iterator()))
                .mapKeyValue(RecipeIngredientType::create)
                .toList();
        }
        throw new IllegalArgumentException("There are more inputs than known Input types");
    }

    public static ItemStack parseItemStack(JsonElement json) {
        if (json.isJsonObject()) {
            return ShapedRecipe.itemStackFromJson(json.getAsJsonObject());
        }
        else {
            String resultJson = json.getAsString();
            ResourceLocation name = new ResourceLocation(resultJson);
            return new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(name)).orElseThrow(() -> new IllegalStateException("Item: " + resultJson + " does not exist")));
        }
    }

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

    public static <R extends BaseRecipeImpl<?, ?, ? super R>> int compareCount(R first, R second) {
        return second.compareInputCount(first);
    }

    public static JsonArray serializeConditions(Collection<ICondition> conditions) {
        JsonArray jsonConditions = new JsonArray();
        for (ICondition condition : conditions) {
            jsonConditions.add(CraftingHelper.serialize(condition));
        }
        return jsonConditions;
    }

    public static Fluid deserializeFluid(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(name);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
        if (fluid == null || fluid == Fluids.EMPTY) {
            throw new IllegalArgumentException("Fluid '" + resourceLocation + "' not found");
        }
        return fluid;
    }

    public static ResourceLocation createId(ItemLike input, ItemStack output) {
        return location("%s_to_%s".formatted(GtUtil.itemName(input), GtUtil.itemName(output)));
    }

    public static ResourceLocation createId(TagKey<Item> input, ItemStack output) {
        return location("%s_to_%s".formatted(GtUtil.tagName(input), GtUtil.itemName(output)));
    }

    private RecipeUtil() {}
}
