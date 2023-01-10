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

public class MIMORecipeType<T extends MIMORecipe<O>, O> implements BaseRecipeType<T> {
    private final List<? extends RecipeIngredientType<? extends RecipeIngredient<ItemStack>>> inputTypes;
    private final List<RecipeOutputType<O>> outputTypes;
    private final MIMORecipeFactory<T, O> factory;

    public MIMORecipeType(int inputCount, List<RecipeOutputType<O>> outputTypes, MIMORecipeFactory<T, O> factory) {
        this.inputTypes = StreamEx.constant(ModRecipeIngredientTypes.ITEM, inputCount).toList();
        this.outputTypes = outputTypes;
        this.factory = factory;
    }

    public List<? extends RecipeOutputType<O>> getOutputTypes() {
        return this.outputTypes;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        List<RecipeIngredient<ItemStack>> inputs = RecipeIngredient.parseInputs(inputJson);
        List<O> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromJson(outputJson))
            .toList();

        return this.factory.create(recipeId, inputs, outputs);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<? extends RecipeIngredient<ItemStack>> inputs = StreamEx.of(this.inputTypes)
            .map(type -> type.create(buffer))
            .toList();
        List<O> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromNetwork(buffer))
            .toList();
        return this.factory.create(recipeId, inputs, outputs);
    }

    public interface MIMORecipeFactory<T extends MIMORecipe<O>, O> {
        T create(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<O> outputs);
    }
}
