package dev.su5ed.gtexperimental.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

/**
 * Multi Input, Multi Output recipe
 */
public abstract class MIMORecipe extends BaseRecipe<MIMORecipeType<?>, MIMORecipe.Input, MIMORecipe> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final List<ItemStack> outputs;
    protected final int duration;
    protected final double energyCost;

    public MIMORecipe(MIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, int duration, double energyCost) {
        super(type, serializer, id);

        this.inputs = inputs;
        this.outputs = outputs;
        this.duration = duration;
        this.energyCost = energyCost;

        RecipeUtil.validateInputList(this.id, "inputs", this.inputs, this.type.inputTypes.size());
        RecipeUtil.validateItemList(this.id, "outputs", this.outputs, this.type.outputTypes.size());
    }

    public List<? extends RecipeIngredient<ItemStack>> getInputs() {
        return this.inputs;
    }

    public List<ItemStack> getOutputs() {
        return this.outputs;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getEnergyCost() {
        return this.energyCost;
    }

    @Override
    public boolean matches(Input input) {
        return this.inputs.size() == input.items.size() && EntryStream.zip(this.inputs, input.items).allMatch(Predicate::test);
    }

    @Override
    public int compareInputCount(MIMORecipe other) {
        return StreamEx.of(this.inputs).mapToInt(RecipeIngredient::getCount).sum()
            - StreamEx.of(other.inputs).mapToInt(RecipeIngredient::getCount).sum();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        for (RecipeIngredient<ItemStack> input : this.inputs) {
            input.toNetwork(buffer);
        }

        for (int i = 0; i < this.type.outputTypes.size(); i++) {
            RecipeOutputType<ItemStack> outputType = this.type.outputTypes.get(i);
            outputType.toNetwork(buffer, this.outputs.get(i));
        }

        buffer.writeInt(this.duration);
        buffer.writeDouble(this.energyCost);
    }

    public record Input(List<ItemStack> items) {}
}
