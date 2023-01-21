package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Single Input, Multi Output recipe
 */
public abstract class SIMORecipe extends BaseRecipeImpl<SIMORecipeType<?>, SIMORecipe.Input, SIMORecipe> {
    protected final RecipeIngredient<ItemStack> input;
    protected final List<ItemStack> output;

    public SIMORecipe(SIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output) {
        super(type, serializer, id);

        this.input = input;
        this.output = output;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateItemList(this.id, "outputs", this.output, this.type.outputTypes.size());
    }

    public List<ItemStack> getOutput() {
        return this.output;
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
        ModRecipeOutputTypes.toNetwork(this.type.outputTypes, this.output, buffer);
    }

    public record Input(ItemStack item) {}
}
