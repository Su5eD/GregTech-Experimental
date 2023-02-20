package dev.su5ed.gtexperimental.recipe.type;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Single Input, Multi Output recipe
 */
public class SIMORecipe<IN, OUT> extends BaseRecipeImpl<SIMORecipeType<?, IN, OUT>, IN, SIMORecipe<IN, OUT>> {
    protected final RecipeIngredient<IN> input;
    protected final List<OUT> output;
    protected final RecipePropertyMap properties;

    public static SIMORecipe<Either<ItemStack, FluidStack>, Either<ItemStack, FluidStack>> industrialElectrolyzer(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.INDUSTRIAL_ELECTROLYZER.get(), ModRecipeSerializers.INDUSTRIAL_ELECTROLYZER.get(), id, input, output, properties);
    }

    public static SIMORecipe<ItemStack, ItemStack> lathe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.LATHE.get(), ModRecipeSerializers.LATHE.get(), id, input, output, properties);
    }

    public static SIMORecipe<Either<ItemStack, FluidStack>, ItemStack> hotFuel(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<ItemStack> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.HOT_FUEL.get(), ModRecipeSerializers.HOT_FUEL.get(), id, input, output, properties, true);
    }

    public SIMORecipe(SIMORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, List<OUT> output, int duration, double energyCost) {
        this(type, serializer, id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }

    public SIMORecipe(SIMORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, List<OUT> output, RecipePropertyMap properties) {
        this(type, serializer, id, input, output, properties, false);
    }

    public SIMORecipe(SIMORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, List<OUT> output, RecipePropertyMap properties, boolean allowEmptyOutput) {
        super(type, serializer, id);

        this.input = input;
        this.output = output;
        this.properties = properties;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateOutputList(this.id, "outputs", this.type.outputType, this.type.outputCount, this.output, allowEmptyOutput);
        this.properties.validate(this.id, this.type.properties);
    }

    public RecipeIngredient<IN> getInput() {
        return this.input;
    }

    public List<OUT> getOutput() {
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
    public boolean matches(IN input) {
        return this.input.test(input);
    }

    @Override
    public int compareInputCount(SIMORecipe<IN, OUT> other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        ModRecipeOutputTypes.toNetwork(this.type.outputType, this.type.outputCount, this.output, buffer);
        this.properties.toNetwork(buffer);
    }
}
