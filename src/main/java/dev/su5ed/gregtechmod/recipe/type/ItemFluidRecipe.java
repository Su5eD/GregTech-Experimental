package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class ItemFluidRecipe extends BaseRecipe<ItemFluidRecipeType<? extends ItemFluidRecipe>, ItemFluidRecipe.Input, ItemFluidRecipe> {
    protected final RecipeIngredient<ItemStack> input;
    protected final RecipeIngredient<FluidStack> fluid;
    protected final List<ItemStack> outputs;

    public ItemFluidRecipe(ItemFluidRecipeType<? extends ItemFluidRecipe> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs) {
        super(type, serializer, id);

        this.input = input;
        this.fluid = fluid;
        this.outputs = outputs;
    }

    public List<ItemStack> getOutputs() {
        return this.outputs;
    }

    @Override
    public boolean matches(Input input) {
        return this.input.test(input.item) && this.fluid.test(input.fluid);
    }

    @Override
    public int compareInputCount(ItemFluidRecipe other) {
        return this.input.getCount() - other.input.getCount(); // TODO compare fluid
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.fluid.toNetwork(buffer);
        for (int i = 0; i < this.type.outputTypes.size(); i++) {
            RecipeOutputType<ItemStack> outputType = this.type.outputTypes.get(i);
            outputType.toNetwork(buffer, this.outputs.get(i));
        }
    }
    
    public record Input(ItemStack item, FluidStack fluid) {}
}
