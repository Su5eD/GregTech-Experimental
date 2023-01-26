package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class MIMORecipeType<R extends MIMORecipe> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<ItemStack>> inputType;
    public final int inputCount;
    public final RecipeOutputType<ItemStack> outputType;
    public final int outputCount;
    private final MIMORecipeFactory<R> factory;

    public MIMORecipeType(ResourceLocation name, int inputCount, int outputCount, MIMORecipeFactory<R> factory) {
        super(name);

        this.inputType = ModRecipeIngredientTypes.ITEM;
        this.inputCount = inputCount;
        this.outputType = ModRecipeOutputTypes.ITEM;
        this.outputCount = outputCount;
        this.factory = factory;
    }

    public RecipeOutputType<ItemStack> getOutputType() {
        return this.outputType;
    }

    public int getOutputCount() {
        return outputCount;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonArray inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonArray outputJson = GsonHelper.getAsJsonArray(serializedRecipe, "output");

        List<? extends RecipeIngredient<ItemStack>> inputs = RecipeUtil.parseInputs(this.inputType, this.inputCount, inputJson);
        List<ItemStack> outputs = RecipeUtil.parseOutputs(this.outputType, this.outputCount, outputJson);
        int duration = GsonHelper.getAsInt(serializedRecipe, "duration");
        double energyCost = GsonHelper.getAsDouble(serializedRecipe, "energyCost");

        return this.factory.create(recipeId, inputs, outputs, duration, energyCost);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<? extends RecipeIngredient<ItemStack>> inputs = ModRecipeIngredientTypes.fromNetwork(this.inputType, this.inputCount, buffer);
        List<ItemStack> outputs = ModRecipeOutputTypes.fromNetwork(this.outputType, this.outputCount, buffer);
        int duration = buffer.readInt();
        double energyCost = buffer.readDouble();
        return this.factory.create(recipeId, inputs, outputs, duration, energyCost);
    }

    public interface MIMORecipeFactory<T extends MIMORecipe> {
        T create(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, int duration, double energyCost);
    }
}
