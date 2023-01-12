package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class SIMORecipeType<T extends SIMORecipe> implements BaseRecipeType<T> {
    protected final RecipeIngredientType<? extends RecipeIngredient<ItemStack>> inputType;
    protected final List<RecipeOutputType<ItemStack>> outputTypes;
    protected final SIMORecipeFactory<T> factory;

    public SIMORecipeType(List<RecipeOutputType<ItemStack>> outputTypes, SIMORecipeFactory<T> factory) {
        this.inputType = ModRecipeIngredientTypes.ITEM;
        this.outputTypes = outputTypes;
        this.factory = factory;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        RecipeIngredient<ItemStack> input = RecipeIngredient.parseItem(inputJson);
        List<ItemStack> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromJson(outputJson))
            .toList();

        return this.factory.create(recipeId, input, outputs);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<ItemStack> input = this.inputType.create(buffer);
        List<ItemStack> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromNetwork(buffer))
            .toList();
        return this.factory.create(recipeId, input, outputs);
    }

    public interface SIMORecipeFactory<T extends SIMORecipe> {
        T create(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs);
    }
}
