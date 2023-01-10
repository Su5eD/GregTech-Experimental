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

public class MISORecipeType<T extends MISORecipe<O>, O> implements BaseRecipeType<T> {
    private final List<? extends RecipeIngredientType<? extends RecipeIngredient<ItemStack>>> inputTypes;
    private final RecipeOutputType<O> outputType;
    private final MISORecipeFactory<T, O> factory;

    public MISORecipeType(int inputCount, RecipeOutputType<O> outputType, MISORecipeFactory<T, O> factory) {
        this.inputTypes = StreamEx.constant(ModRecipeIngredientTypes.ITEM, inputCount).toList();
        this.outputType = outputType;
        this.factory = factory;
    }

    public RecipeOutputType<O> getOutputType() {
        return this.outputType;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        List<RecipeIngredient<ItemStack>> inputs = RecipeIngredient.parseInputs(inputJson);
        O output = this.outputType.fromJson(outputJson);

        return this.factory.create(recipeId, inputs, output);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<? extends RecipeIngredient<ItemStack>> inputs = StreamEx.of(this.inputTypes)
            .map(type -> type.create(buffer))
            .toList();
        O output = this.outputType.fromNetwork(buffer);
        return this.factory.create(recipeId, inputs, output);
    }

    public interface MISORecipeFactory<T extends MISORecipe<O>, O> {
        T create(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, O output);
    }
}
