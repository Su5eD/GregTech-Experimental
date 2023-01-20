package dev.su5ed.gtexperimental.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Single Input, Single Output recipe
 */
public abstract class SISORecipe extends BaseRecipe<SISORecipeType<?>, SISORecipe.Input, SISORecipe> {
    protected final RecipeIngredient<ItemStack> input;
    protected final ItemStack output;
    protected final int duration;
    protected final double energyCost;

    public SISORecipe(SISORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        super(type, serializer, id);

        this.input = input;
        this.output = output;
        this.duration = duration;
        this.energyCost = energyCost;

        RecipeUtil.validateInput(this.id, "input", this.input);
        RecipeUtil.validateItem(this.id, "output", this.output);
    }

    public RecipeIngredient<ItemStack> getInput() {
        return this.input;
    }

    public ItemStack getOutput() {
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
        return this.input.test(input.item);
    }

    @Override
    public int compareInputCount(SISORecipe other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.type.outputType.toNetwork(buffer, this.output);
        buffer.writeInt(this.duration);
        buffer.writeDouble(this.energyCost);
    }

    public record Input(ItemStack item) {}
}
