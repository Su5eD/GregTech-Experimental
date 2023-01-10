package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Multi Input, Single Output recipe
 */
public abstract class MISORecipe<O> extends BaseRecipe<MISORecipeType<?, O>> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final O output;

    public MISORecipe(MISORecipeType<?, O> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, O output) {
        super(type, serializer, id);
        this.inputs = inputs;
        this.output = output;
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

    public O getOutput() {
        return this.output;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        for (RecipeIngredient<ItemStack> input : this.inputs) {
            input.toNetwork(buffer);
        }
        this.type.getOutputType().toNetwork(buffer, this.output);
    }
}
