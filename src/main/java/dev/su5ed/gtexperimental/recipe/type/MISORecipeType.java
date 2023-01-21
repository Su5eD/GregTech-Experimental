package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class MISORecipeType<R extends MISORecipe> extends BaseRecipeTypeImpl<R> {
    public final List<? extends RecipeIngredientType<? extends RecipeIngredient<ItemStack>>> inputTypes;
    public final RecipeOutputType<ItemStack> outputType;
    private final MISORecipeFactory<R> factory;

    public MISORecipeType(ResourceLocation name, int inputCount, RecipeOutputType<ItemStack> outputType, MISORecipeFactory<R> factory) {
        super(name);

        this.inputTypes = StreamEx.constant(ModRecipeIngredientTypes.ITEM, inputCount).toList();
        this.outputType = outputType;
        this.factory = factory;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonArray inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        List<? extends RecipeIngredient<ItemStack>> inputs = RecipeUtil.parseInputs(this.inputTypes, inputJson);
        ItemStack output = this.outputType.fromJson(outputJson);
        int duration = GsonHelper.getAsInt(serializedRecipe, "duration");
        double energyCost = GsonHelper.getAsDouble(serializedRecipe, "energyCost");

        return this.factory.create(recipeId, inputs, output, duration, energyCost);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<? extends RecipeIngredient<ItemStack>> inputs = ModRecipeIngredientTypes.fromNetwork(this.inputTypes, buffer);
        ItemStack output = this.outputType.fromNetwork(buffer);
        int duration = buffer.readInt();
        double energyCost = buffer.readDouble();
        return this.factory.create(recipeId, inputs, output, duration, energyCost);
    }

    public interface MISORecipeFactory<T extends MISORecipe> {
        T create(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost);
    }
}
