package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface RecipeIngredient<T> extends Predicate<T> {
    RecipeIngredientType<?> getType();
    
    int getCount();
    
    boolean isEmpty();

    void toNetwork(FriendlyByteBuf buffer);
    
    JsonElement toJson();

    static List<? extends RecipeIngredient<ItemStack>> parseInputs(List<? extends RecipeIngredientType<? extends RecipeIngredient<ItemStack>>> inputTypes, JsonArray json) {
        if (inputTypes.size() == json.size()) {
            return StreamEx.of(inputTypes)
                .zipWith(StreamEx.of(json.getAsJsonArray().iterator()))
                .mapKeyValue(RecipeIngredientType::create)
                .toList();
        }
        throw new IllegalArgumentException("Input type and json sizes differ");
    }

    static RecipeIngredient<FluidStack> parseFluid(JsonElement json) {
        return ModRecipeIngredientTypes.FLUID.create(json);
    }

    static RecipeIngredient<ItemStack> parseItem(JsonElement json) {
        if (json.isJsonObject()) {
            // Parse our own types here
            Ingredient ingredient = Ingredient.fromJson(json);
            return new VanillaRecipeIngredient(ingredient);
        }
        throw new IllegalArgumentException("Not a JSON object");
    }

    static ItemStack parseItemStack(JsonElement json) {
        if (json.isJsonObject()) {
            return ShapedRecipe.itemStackFromJson(json.getAsJsonObject());
        }
        else {
            String resultJson = json.getAsString();
            ResourceLocation name = new ResourceLocation(resultJson);
            return new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(name)).orElseThrow(() -> new IllegalStateException("Item: " + resultJson + " does not exist")));
        }
    }
}
