package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

import java.util.List;

public class SIMORecipeType<R extends SIMORecipe> extends BaseRecipeTypeImpl<R> {
    protected final RecipeIngredientType<? extends RecipeIngredient<ItemStack>> inputType;
    protected final List<RecipeOutputType<ItemStack>> outputTypes;
    protected final SIMORecipeFactory<R> factory;

    public SIMORecipeType(ResourceLocation name, List<RecipeOutputType<ItemStack>> outputTypes, SIMORecipeFactory<R> factory) {
        super(name);

        this.inputType = ModRecipeIngredientTypes.ITEM;
        this.outputTypes = outputTypes;
        this.factory = factory;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = GsonHelper.getAsJsonObject(serializedRecipe, "input");
        JsonArray outputJson = GsonHelper.getAsJsonArray(serializedRecipe, "output");

        RecipeIngredient<ItemStack> input = this.inputType.create(inputJson);
        List<ItemStack> outputs = RecipeOutputType.parseOutputs(this.outputTypes, outputJson);

        return this.factory.create(recipeId, input, outputs);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<ItemStack> input = this.inputType.create(buffer);
        List<ItemStack> outputs =  ModRecipeOutputTypes.fromNetwork(this.outputTypes, buffer);
        return this.factory.create(recipeId, input, outputs);
    }

    public interface SIMORecipeFactory<T extends SIMORecipe> {
        T create(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs);
    }
}
