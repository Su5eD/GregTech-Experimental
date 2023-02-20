package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class IFMORecipeType<R extends IFMORecipe> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<ItemStack>> inputType;
    public final RecipeIngredientType<? extends RecipeIngredient<FluidStack>> fluidType;
    public final RecipeOutputType<ItemStack> outputType;
    public final int outputCount;
    public final List<RecipeProperty<?>> properties;
    public final IFMORecipeFactory<R> factory;

    public IFMORecipeType(ResourceLocation name, int outputCount, List<RecipeProperty<?>> properties, IFMORecipeFactory<R> factory) {
        super(name);

        this.inputType = ModRecipeIngredientTypes.ITEM;
        this.fluidType = ModRecipeIngredientTypes.FLUID;
        this.outputType = ModRecipeOutputTypes.ITEM;
        this.outputCount = outputCount;
        this.properties = properties;
        this.factory = factory;
    }

    public RecipeOutputType<ItemStack> getOutputType() {
        return this.outputType;
    }

    public int getOutputCount() {
        return this.outputCount;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonObject inputJson = GsonHelper.getAsJsonObject(serializedRecipe, "input");
        JsonObject fluidJson = GsonHelper.getAsJsonObject(serializedRecipe, "fluid");
        JsonArray outputJson = GsonHelper.getAsJsonArray(serializedRecipe, "output");

        RecipeIngredient<ItemStack> input = this.inputType.create(inputJson);
        RecipeIngredient<FluidStack> fluid = this.fluidType.create(fluidJson);
        List<ItemStack> outputs = RecipeUtil.parseOutputs(this.outputType, this.outputCount, outputJson);
        RecipePropertyMap properties = RecipePropertyMap.fromJson(recipeId, this.properties, serializedRecipe);

        return this.factory.create(recipeId, input, fluid, outputs, properties);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<ItemStack> input = this.inputType.create(buffer);
        RecipeIngredient<FluidStack> fluid = this.fluidType.create(buffer);
        List<ItemStack> outputs = ModRecipeOutputTypes.fromNetwork(this.outputType, this.outputCount, buffer);
        RecipePropertyMap properties = RecipePropertyMap.fromNetwork(this.properties, buffer);
        
        return this.factory.create(recipeId, input, fluid, outputs, properties);
    }

    public interface IFMORecipeFactory<T extends IFMORecipe> {
        T create(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs, RecipePropertyMap properties);
    }
}