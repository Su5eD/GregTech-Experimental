package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

/**
 * Multi Input, Single Output recipe
 */
public abstract class MISORecipe<O> extends BaseRecipe<MISORecipeType<?, O>, MISORecipe.Input, MISORecipe<O>> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final O output;

    public MISORecipe(MISORecipeType<?, O> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, O output) {
        super(type, serializer, id);
        this.inputs = inputs;
        this.output = output;
    }

    public O getOutput() {
        return this.output;
    }

    @Override
    public boolean matches(Input input) {
        return this.inputs.size() == input.items.size() && EntryStream.zip(this.inputs, input.items).allMatch(Predicate::test);
    }

    @Override
    public int compareInputCount(MISORecipe<O> other) {
        return StreamEx.of(this.inputs).mapToInt(RecipeIngredient::getCount).sum()
            - StreamEx.of(other.inputs).mapToInt(RecipeIngredient::getCount).sum();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        for (RecipeIngredient<ItemStack> input : this.inputs) {
            input.toNetwork(buffer);
        }
        this.type.getOutputType().toNetwork(buffer, this.output);
    }

    public record Input(List<ItemStack> items) {}
}
