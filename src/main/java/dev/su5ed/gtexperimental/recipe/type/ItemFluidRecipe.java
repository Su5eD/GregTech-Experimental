package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class ItemFluidRecipe extends BaseRecipeImpl<ItemFluidRecipeType<? extends ItemFluidRecipe>, ItemFluidRecipe.Input, ItemFluidRecipe> {
    protected final RecipeIngredient<ItemStack> input;
    protected final RecipeIngredient<FluidStack> fluid;
    protected final List<ItemStack> output;

    public ItemFluidRecipe(ItemFluidRecipeType<? extends ItemFluidRecipe> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> output) {
        super(type, serializer, id);

        this.input = input;
        this.fluid = fluid;
        this.output = output;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateInput(this.id, "fluid", this.fluid);
        RecipeUtil.validateItemList(this.id, "outputs", this.output, this.type.outputTypes.size());
    }

    public List<ItemStack> getOutput() {
        return this.output;
    }

    @Override
    public boolean matches(Input input) {
        return this.input.test(input.item) && this.fluid.test(input.fluid);
    }

    @Override
    public int compareInputCount(ItemFluidRecipe other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.fluid.toNetwork(buffer);
        ModRecipeOutputTypes.toNetwork(this.type.outputTypes, this.output, buffer);
    }

    public record Input(ItemStack item, FluidStack fluid) {}
}
