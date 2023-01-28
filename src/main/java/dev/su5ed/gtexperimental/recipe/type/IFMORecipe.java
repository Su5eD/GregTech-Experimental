package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class IFMORecipe extends BaseRecipeImpl<IFMORecipeType<? extends IFMORecipe>, IFMORecipe.Input, IFMORecipe> {
    protected final RecipeIngredient<ItemStack> input;
    protected final RecipeIngredient<FluidStack> fluid;
    protected final List<ItemStack> output;
    protected final RecipePropertyMap properties;

    public IFMORecipe(IFMORecipeType<? extends IFMORecipe> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> output, RecipePropertyMap properties) {
        super(type, serializer, id);

        this.input = input;
        this.fluid = fluid;
        this.output = output;
        this.properties = properties;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateInput(this.id, "fluid", this.fluid);
        RecipeUtil.validateOutputList(this.id, "outputs", this.type.outputType, this.type.outputCount, this.output);
        this.properties.validate(this.id, this.type.properties);
    }

    public RecipeIngredient<ItemStack> getInput() {
        return this.input;
    }

    public RecipeIngredient<FluidStack> getFluid() {
        return this.fluid;
    }

    public List<ItemStack> getOutput() {
        return this.output;
    }

    public RecipePropertyMap getProperties() {
        return this.properties;
    }

    public int getDuration() {
        return this.properties.get(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost() {
        return this.properties.get(ModRecipeProperty.ENERGY_COST);
    }

    @Override
    public boolean matches(Input input) {
        return this.input.test(input.item) && this.fluid.test(input.fluid);
    }

    @Override
    public int compareInputCount(IFMORecipe other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.fluid.toNetwork(buffer);
        ModRecipeOutputTypes.toNetwork(this.type.outputType, this.type.outputCount, this.output, buffer);
    }

    public record Input(ItemStack item, FluidStack fluid) {}
}
