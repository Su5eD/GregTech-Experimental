package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class RecipeUtil {

    public static RecipeIngredient<FluidStack> parseFluid(JsonElement json) {
        return ModRecipeIngredientTypes.FLUID.create(json);
    }

    public static RecipeIngredient<ItemStack> parseItem(JsonElement json) {
        if (json.isJsonObject()) {
            Ingredient ingredient = Ingredient.fromJson(json);
            return new VanillaRecipeIngredient(ingredient);
        }
        throw new IllegalArgumentException("Not a JSON object");
    }

    public static List<? extends RecipeIngredient<ItemStack>> parseInputs(List<? extends RecipeIngredientType<? extends RecipeIngredient<ItemStack>>> inputTypes, JsonArray json) {
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

    private RecipeUtil() {}
}
