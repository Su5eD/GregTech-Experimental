package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class SISORecipeType<R extends SISORecipe> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<ItemStack>> inputType;
    public final RecipeOutputType<ItemStack> outputType;
    protected final SISORecipeFactory<R> factory;

    public SISORecipeType(ResourceLocation name, RecipeOutputType<ItemStack> outputType, SISORecipeFactory<R> factory) {
        super(name);

        this.inputType = ModRecipeIngredientTypes.ITEM;
        this.outputType = outputType;
        this.factory = factory;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        RecipeIngredient<ItemStack> input = RecipeIngredient.parseItem(inputJson);
        ItemStack output = ModRecipeOutputTypes.ITEM.fromJson(outputJson);
        int duration = GsonHelper.getAsInt(serializedRecipe, "duration");
        double energyCost = GsonHelper.getAsDouble(serializedRecipe, "energyCost");

        return this.factory.create(recipeId, input, output, duration, energyCost);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<ItemStack> input = this.inputType.create(buffer);
        ItemStack output = ModRecipeOutputTypes.ITEM.fromNetwork(buffer);
        int duration = buffer.readInt();
        double energyCost = buffer.readDouble();
        return this.factory.create(recipeId, input, output, duration, energyCost);
    }

    public interface SISORecipeFactory<T extends SISORecipe> {
        T create(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost);
    }
}
