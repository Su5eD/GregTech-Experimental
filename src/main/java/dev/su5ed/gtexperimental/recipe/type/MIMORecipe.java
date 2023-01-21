package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
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
public abstract class MIMORecipe extends BaseRecipeImpl<MIMORecipeType<?>, MIMORecipe.Input, MIMORecipe> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final List<ItemStack> output;
    protected final int duration;
    protected final double energyCost;

    public MIMORecipe(MIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> output, int duration, double energyCost) {
        super(type, serializer, id);

        this.inputs = inputs;
        this.output = output;
        this.duration = duration;
        this.energyCost = energyCost;

        RecipeUtil.validateInputList(this.id, "inputs", this.inputs, this.type.inputTypes.size());
        RecipeUtil.validateItemList(this.id, "outputs", this.output, this.type.outputTypes.size());
    }

    public List<? extends RecipeIngredient<ItemStack>> getInputs() {
        return this.inputs;
    }

    public List<ItemStack> getOutput() {
        return this.output;
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
        ModRecipeIngredientTypes.toNetwork(this.inputs, buffer);
        ModRecipeOutputTypes.toNetwork(this.type.outputTypes, this.output, buffer);
        buffer.writeInt(this.duration);
        buffer.writeDouble(this.energyCost);
    }

    public record Input(List<ItemStack> items) {}
}
