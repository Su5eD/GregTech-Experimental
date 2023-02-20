package dev.su5ed.gtexperimental.recipe.type;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

/**
 * Single Input, Single Output recipe
 */
public class SISORecipe<IN, OUT> extends BaseRecipeImpl<SISORecipeType<?, IN, OUT>, IN, SISORecipe<IN, OUT>> {
    protected final RecipeIngredient<IN> input;
    protected final OUT output;
    protected final RecipePropertyMap properties;

    public static SISORecipe<FluidStack, FluidStack> denseLiquid(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.DENSE_LIQUID_FUEL.get(), ModRecipeSerializers.DENSE_LIQUID_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<Either<ItemStack, FluidStack>, FluidStack> dieselFuel(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.DIESEL_FUEL.get(), ModRecipeSerializers.DIESEL_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<FluidStack, FluidStack> gasFuel(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.GAS_FUEL.get(), ModRecipeSerializers.GAS_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<Either<ItemStack, FluidStack>, ItemStack> magic(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.MAGIC_FUEL.get(), ModRecipeSerializers.MAGIC_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<FluidStack, FluidStack> plasma(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.PLASMA_FUEL.get(), ModRecipeSerializers.PLASMA_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<FluidStack, FluidStack> steam(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.STEAM_FUEL.get(), ModRecipeSerializers.STEAM_FUEL.get(), id, input, output, properties, true);
    }

    public SISORecipe(SISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, int duration, double energyCost) {
        this(type, serializer, id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }

    public SISORecipe(SISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, RecipePropertyMap properties) {
        this(type, serializer, id, input, output, properties, false);
    }

    public SISORecipe(SISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, RecipePropertyMap properties, boolean allowEmptyOutput) {
        super(type, serializer, id);

        this.input = input;
        this.output = output;
        this.properties = properties;

        RecipeUtil.validateInput(this.id, "input", this.input);
        this.type.outputType.validate(this.id, "output", this.output, allowEmptyOutput);
        this.properties.validate(this.id, this.type.properties);
    }

    public RecipeIngredient<IN> getInput() {
        return this.input;
    }

    public OUT getOutput() {
        return this.output;
    }

    public int getDuration() {
        return this.properties.get(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost() {
        return this.properties.get(ModRecipeProperty.ENERGY_COST);
    }

    public RecipePropertyMap getProperties() {
        return this.properties;
    }

    @Override
    public boolean matches(IN input) {
        return this.input.test(input);
    }

    @Override
    public int compareInputCount(SISORecipe<IN, OUT> other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.type.outputType.toNetwork(buffer, this.output);
        this.properties.toNetwork(buffer);
    }
}
