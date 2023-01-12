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
public abstract class MISORecipe extends BaseRecipe<MISORecipeType<?>, MISORecipe.Input, MISORecipe> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final ItemStack output;

    public MISORecipe(MISORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output) {
        super(type, serializer, id);
        this.inputs = inputs;
        this.output = output;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public void validate() {
        super.validate();

        RecipeUtil.validateInputList(this.id, "inputs", this.inputs, this.type.inputTypes.size());
        RecipeUtil.validateItem(this.id, "output", this.output);
    }

    @Override
    public boolean matches(Input input) {
        return this.inputs.size() == input.items.size() && EntryStream.zip(this.inputs, input.items).allMatch(Predicate::test);
    }

    @Override
    public int compareInputCount(MISORecipe other) {
        return StreamEx.of(this.inputs).mapToInt(RecipeIngredient::getCount).sum()
            - StreamEx.of(other.inputs).mapToInt(RecipeIngredient::getCount).sum();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        for (RecipeIngredient<ItemStack> input : this.inputs) {
            input.toNetwork(buffer);
        }
        this.type.outputType.toNetwork(buffer, this.output);
    }

    public record Input(List<ItemStack> items) {}
}
