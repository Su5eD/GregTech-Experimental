package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class ItemFluidRecipe extends BaseRecipe<ItemFluidRecipeType<? extends ItemFluidRecipe>> {
    protected final RecipeIngredient<ItemStack> input;
    protected final RecipeIngredient<FluidStack> fluid;
    protected final List<ItemStack> outputs;

    public ItemFluidRecipe(ItemFluidRecipeType<? extends ItemFluidRecipe> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs) {
        super(type, serializer, id);

        this.input = input;
        this.fluid = fluid;
        this.outputs = outputs;
    }

    public boolean matches(ItemStack input, FluidStack fluid) {
        return this.input.test(input) && this.fluid.test(fluid);
    }

    public List<ItemStack> getOutputs() {
        return this.outputs;
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
}
