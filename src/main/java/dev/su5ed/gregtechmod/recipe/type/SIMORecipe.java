package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Single Input, Multi Output recipe
 */
public abstract class SIMORecipe extends BaseRecipe<SIMORecipeType<?>, SIMORecipe.Input, SIMORecipe> {
    protected final RecipeIngredient<ItemStack> input;
    protected final List<ItemStack> outputs;

    public SIMORecipe(SIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs) {
        super(type, serializer, id);

        this.input = input;
        this.outputs = outputs;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateItemList(this.id, "outputs", this.outputs, this.type.outputTypes.size());
    }

    public List<ItemStack> getOutputs() {
        return this.outputs;
    }

    @Override
    public boolean matches(Input input) {
        return this.input.test(input.item);
    }

    @Override
    public int compareInputCount(SIMORecipe other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        for (int i = 0; i < this.type.outputTypes.size(); i++) {
            RecipeOutputType<ItemStack> outputType = this.type.outputTypes.get(i);
            outputType.toNetwork(buffer, this.outputs.get(i));
        }
    }

    public record Input(ItemStack item) {}
}
