package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

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
        JsonElement inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        RecipeIngredient<ItemStack> input = RecipeIngredient.parseItem(inputJson);
        List<ItemStack> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromJson(outputJson))
            .toList();

        return this.factory.create(recipeId, input, outputs);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
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
