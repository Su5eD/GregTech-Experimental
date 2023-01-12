package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class MIMORecipeType<R extends MIMORecipe> extends BaseRecipeTypeImpl<R> {
    public final List<? extends RecipeIngredientType<? extends RecipeIngredient<ItemStack>>> inputTypes;
    public final List<RecipeOutputType<ItemStack>> outputTypes;
    private final MIMORecipeFactory<R> factory;

    public MIMORecipeType(ResourceLocation name, int inputCount, List<RecipeOutputType<ItemStack>> outputTypes, MIMORecipeFactory<R> factory) {
        super(name);

        this.inputTypes = StreamEx.constant(ModRecipeIngredientTypes.ITEM, inputCount).toList();
        this.outputTypes = outputTypes;
        this.factory = factory;
    }

    public List<? extends RecipeOutputType<ItemStack>> getOutputTypes() {
        return this.outputTypes;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonArray inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        List<? extends RecipeIngredient<ItemStack>> inputs = RecipeIngredient.parseInputs(this.inputTypes, inputJson);
        List<ItemStack> outputs = StreamEx.of(this.outputTypes) // FIXME this won't do
            .map(type -> type.fromJson(outputJson))
            .toList();

        return this.factory.create(recipeId, inputs, outputs);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<? extends RecipeIngredient<ItemStack>> inputs = StreamEx.of(this.inputTypes)
            .map(type -> type.create(buffer))
            .toList();
        List<ItemStack> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromNetwork(buffer))
            .toList();
        return this.factory.create(recipeId, inputs, outputs);
    }

    public interface MIMORecipeFactory<T extends MIMORecipe> {
        T create(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs);
    }
}
