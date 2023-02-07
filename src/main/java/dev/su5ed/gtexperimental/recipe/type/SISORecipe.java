package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Single Input, Single Output recipe
 */
public abstract class SISORecipe<T> extends BaseRecipeImpl<SISORecipeType<?, T>, T, SISORecipe<T>> {
    protected final RecipeIngredient<T> input;
    protected final T output;
    protected final RecipePropertyMap properties;
    
    public SISORecipe(SISORecipeType<?, T> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<T> input, T output, int duration, double energyCost) {
        this(type, serializer, id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }

    public SISORecipe(SISORecipeType<?, T> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<T> input, T output, RecipePropertyMap properties) {
        super(type, serializer, id);

        this.input = input;
        this.output = output;
        this.properties = properties;

        RecipeUtil.validateInput(this.id, "input", this.input);
        this.type.outputType.validate(this.id, "output", this.output);
        this.properties.validate(this.id, this.type.properties);
    }

    public RecipeIngredient<T> getInput() {
        return this.input;
    }

    public T getOutput() {
        return this.output;
    }

    public int getDuration() {
        return this.properties.get(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost() {
        return this.properties.get(ModRecipeProperty.ENERGY_COST);
    }

    @Override
    public boolean matches(T input) {
        return this.input.test(input);
    }

    @Override
    public int compareInputCount(SISORecipe<T> other) {
        return this.input.getCount() - other.input.getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.type.outputType.toNetwork(buffer, this.output);
        this.properties.toNetwork(buffer);
    }
}
