package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class ItemFluidRecipeType<R extends ItemFluidRecipe> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<ItemStack>> inputType;
    public final RecipeIngredientType<? extends RecipeIngredient<FluidStack>> fluidType;
    public final List<RecipeOutputType<ItemStack>> outputTypes;
    public final ItemFluidRecipeFactory<R> factory;

    public ItemFluidRecipeType(ResourceLocation name, int outputCount, ItemFluidRecipeFactory<R> factory) {
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
        JsonElement outputJson = serializedRecipe.get("output");

        RecipeIngredient<ItemStack> input = RecipeIngredient.parseItem(inputJson);
        RecipeIngredient<FluidStack> fluid = RecipeIngredient.parseFluid(fluidJson);
        List<ItemStack> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromJson(outputJson))
            .toList();

        return this.factory.create(recipeId, input, fluid, outputs);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<ItemStack> input = this.inputType.create(buffer);
        RecipeIngredient<FluidStack> fluid = this.fluidType.create(buffer);
        List<ItemStack> outputs = StreamEx.of(this.outputTypes)
            .map(type -> type.fromNetwork(buffer))
            .toList();
        return this.factory.create(recipeId, input, fluid, outputs);
    }

    public interface ItemFluidRecipeFactory<T extends ItemFluidRecipe> {
        T create(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs);
    }
}
