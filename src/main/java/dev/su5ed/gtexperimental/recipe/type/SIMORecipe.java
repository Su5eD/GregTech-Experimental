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
    protected final int duration;
    protected final double energyCost;

    public SIMORecipe(SIMORecipeType<?, T> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<T> input, List<T> output, int duration, double energyCost) {
        super(type, serializer, id);

        this.input = input;
        this.output = output;
        this.duration = duration;
        this.energyCost = energyCost;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateOutputList(this.id, "outputs", this.type.outputType, this.type.outputCount, this.output);
    }

    public RecipeIngredient<T> getInput() {
        return this.input;
    }

    public List<T> getOutput() {
        return this.output;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getEnergyCost() {
        return this.energyCost;
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
        buffer.writeInt(this.duration);
        buffer.writeDouble(this.energyCost);
    }

    public record Input<T>(T item) {}
}
