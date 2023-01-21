package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

/**
 * Multi Input, Single Output recipe
 */
public abstract class MISORecipe<T> extends BaseRecipeImpl<MISORecipeType<?, T>, MISORecipe.Input<T>, MISORecipe<T>> {
    protected final List<? extends RecipeIngredient<T>> inputs;
    protected final T output;
    protected final int duration;
    protected final double energyCost;

    public MISORecipe(MISORecipeType<?, T> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<T>> inputs, T output, int duration, double energyCost) {
        super(type, serializer, id);

        this.inputs = inputs;
        this.output = output;
        this.duration = duration;
        this.energyCost = energyCost;

        RecipeUtil.validateInputList(this.id, "inputs", this.inputs, this.type.inputCount);
        this.type.outputType.validate(this.id, "output", this.output);
        // TODO validate energyCost and duration
    }

    public List<? extends RecipeIngredient<T>> getInputs() {
        return this.inputs;
    }

    public T getOutput() {
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
        return this.inputs.size() == input.items.size() && EntryStream.zip(this.inputs, input.items).allMatch(Predicate::test);
    }

    @Override
    public int compareInputCount(MISORecipe<T> other) {
        return StreamEx.of(this.inputs).mapToInt(RecipeIngredient::getCount).sum()
            - StreamEx.of(other.inputs).mapToInt(RecipeIngredient::getCount).sum();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        ModRecipeIngredientTypes.toNetwork(this.inputs, buffer);
        this.type.outputType.toNetwork(buffer, this.output);
        buffer.writeInt(this.duration);
        buffer.writeDouble(this.energyCost);
    }

    public record Input<T>(List<T> items) {}
}
