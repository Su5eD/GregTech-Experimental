package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Single Input, Multi Output recipe
 */
public abstract class SIMORecipe<T> extends BaseRecipeImpl<SIMORecipeType<?, T>, SIMORecipe.Input<T>, SIMORecipe<T>> {
    protected final RecipeIngredient<T> input;
    protected final List<T> output;
    protected final RecipePropertyMap properties;
    
    public SIMORecipe(SIMORecipeType<?, T> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<T> input, List<T> output, int duration, double energyCost) {
        this(type, serializer, id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }

    public SIMORecipe(SIMORecipeType<?, T> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<T> input, List<T> output, RecipePropertyMap properties) {
        super(type, serializer, id);

        this.input = input;
        this.output = output;
        this.properties = properties;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateOutputList(this.id, "outputs", this.type.outputType, this.type.outputCount, this.output);
        this.properties.validate(this.id, this.type.properties);
    }

    public RecipeIngredient<T> getInput() {
        return this.input;
    }

    public List<T> getOutput() {
        return this.output;
    }

    public int getDuration() {
        return this.properties.get(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost() {
        return this.properties.get(ModRecipeProperty.ENERGY_COST);
    }

    @Override
    public boolean matches(Input<T> input) {
        return this.input.test(input.item);
    }

    @Override
    public int compareInputCount(SIMORecipe<T> other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        ModRecipeOutputTypes.toNetwork(this.type.outputType, this.type.outputCount, this.output, buffer);
        this.properties.toNetwork(buffer);
    }

    public record Input<T>(T item) {}
}
