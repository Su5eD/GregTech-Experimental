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
import net.minecraftforge.fluids.FluidStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class IFMORecipeType<R extends IFMORecipe> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<ItemStack>> inputType;
    public final RecipeIngredientType<? extends RecipeIngredient<FluidStack>> fluidType;
    public final List<RecipeOutputType<ItemStack>> outputTypes;
    public final IFMORecipeFactory<R> factory;

    public IFMORecipeType(ResourceLocation name, int outputCount, IFMORecipeFactory<R> factory) {
        super(name);

        this.inputType = ModRecipeIngredientTypes.ITEM;
        this.fluidType = ModRecipeIngredientTypes.FLUID;
        this.outputTypes = StreamEx.constant(ModRecipeOutputTypes.ITEM, outputCount).toList();
        this.factory = factory;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = GsonHelper.getAsJsonObject(serializedRecipe, "input");
        JsonElement fluidJson = GsonHelper.getAsJsonObject(serializedRecipe, "fluid");
        JsonArray outputJson = GsonHelper.getAsJsonArray(serializedRecipe, "output");

        RecipeIngredient<ItemStack> input = this.inputType.create(inputJson);
        RecipeIngredient<FluidStack> fluid = this.fluidType.create(fluidJson);
        List<ItemStack> outputs = RecipeUtil.parseOutputs(this.outputTypes, outputJson);

        return this.factory.create(recipeId, input, fluid, outputs);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<ItemStack> input = this.inputType.create(buffer);
        RecipeIngredient<FluidStack> fluid = this.fluidType.create(buffer);
        List<ItemStack> outputs = ModRecipeOutputTypes.fromNetwork(this.outputTypes, buffer);
        return this.factory.create(recipeId, input, fluid, outputs);
    }

    public interface IFMORecipeFactory<T extends IFMORecipe> {
        T create(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs);
    }
}
