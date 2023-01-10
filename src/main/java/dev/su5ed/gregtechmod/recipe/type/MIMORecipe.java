package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Multi Input, Multi Output recipe
 */
public abstract class MIMORecipe<O> extends BaseRecipe<MIMORecipeType<?, O>> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final List<O> outputs;

    public MIMORecipe(MIMORecipeType<?, O> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<O> outputs) {
        super(type, serializer, id);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public boolean matches(List<ItemStack> inputs) {
        if (this.inputs.size() == inputs.size()) {
            for (int i = 0; i < this.inputs.size(); i++) {
                if (!this.inputs.get(i).test(inputs.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public List<O> getOutputs() {
        return this.outputs;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        for (RecipeIngredient<ItemStack> input : this.inputs) {
            input.toNetwork(buffer);
        }
        
        List<? extends RecipeOutputType<O>> outputTypes = this.type.getOutputTypes();
        for (int i = 0; i < outputTypes.size(); i++) {
            RecipeOutputType<O> outputType = outputTypes.get(i);
            outputType.toNetwork(buffer, this.outputs.get(i));
        }
    }
}
