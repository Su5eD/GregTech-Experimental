package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeImpl;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class IFMORecipe extends BaseRecipeImpl<IFMORecipeType<? extends IFMORecipe>, IFMORecipe.Input, List<ItemStack>, IFMORecipe> {
    protected final RecipeIngredient<ItemStack> input;
    protected final RecipeIngredient<FluidStack> fluid;

    public static IFMORecipe industrialGrinder(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new IFMORecipe(ModRecipeTypes.INDUSTRIAL_GRINDER.get(), ModRecipeSerializers.INDUSTRIAL_GRINDER.get(), id, input, fluid, outputs, properties.withTransient(b -> b.duration(input.getCount() * 100).energyCost(128)));
    }

    public static IFMORecipe industrialSawmill(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new IFMORecipe(ModRecipeTypes.INDUSTRIAL_SAWMILL.get(), ModRecipeSerializers.INDUSTRIAL_SAWMILL.get(), id, input, fluid, outputs, properties.withTransient(b -> b.duration(input.getCount() * 200).energyCost(32)));
    }

    public IFMORecipe(IFMORecipeType<? extends IFMORecipe> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> output, RecipePropertyMap properties) {
        super(type, serializer, id, output, properties);

        this.input = input;
        this.fluid = fluid;

        this.input.validate(this.id, "input");
        this.fluid.validate(this.id, "fluid");
        this.type.getOutputType().validate(this.id, "outputs", this.output, false);
    }

    public RecipeIngredient<ItemStack> getInput() {
        return this.input;
    }

    public RecipeIngredient<FluidStack> getFluid() {
        return this.fluid;
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
        this.type.getOutputType().toNetwork(buffer, this.output);
    }

    public record Input(ItemStack item, FluidStack fluid) {}
}
