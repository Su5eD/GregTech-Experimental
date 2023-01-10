package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Single Input, Multi Output recipe
 */
public abstract class SIMORecipe<O> extends BaseRecipe<SIMORecipeType<?, O>> {
    protected final RecipeIngredient<ItemStack> input;
    protected final List<O> outputs;

    public SIMORecipe(SIMORecipeType<?, O> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, List<O> outputs) {
        super(type, serializer, id);
        this.input = input;
        this.outputs = outputs;
    }

    public boolean matches(ItemStack input) {
        return this.input.test(input);
    }

    public List<O> getOutputs() {
        return this.outputs;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        for (int i = 0; i < this.type.outputTypes.size(); i++) {
            RecipeOutputType<O> outputType = this.type.outputTypes.get(i);
            outputType.toNetwork(buffer, this.outputs.get(i));
        }
    }
}
