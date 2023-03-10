package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipe<T extends BaseRecipeType<?, OUT>, RIN extends RecipeIngredient<IN>, IN, OUT, C extends ModRecipe<T, RIN, IN, OUT, C>> extends BaseRecipeImpl<T, IN, OUT, C> {
    protected final RIN input;

    public ModRecipe(T type, RecipeSerializer<?> serializer, ResourceLocation id, RIN input, OUT output, int duration, double energyCost) {
        this(type, serializer, id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }

    public ModRecipe(T type, RecipeSerializer<?> serializer, ResourceLocation id, RIN input, OUT output, RecipePropertyMap properties) {
        this(type, serializer, id, input, output, properties, false);
    }

    public ModRecipe(T type, RecipeSerializer<?> serializer, ResourceLocation id, RIN input, OUT output, RecipePropertyMap properties, boolean allowEmptyOutput) {
        super(type, serializer, id, output, properties);

        this.input = input;

        this.input.validate(this.id, "input");
        this.type.getOutputType().validate(this.id, "output", this.output, allowEmptyOutput);
    }

    public RIN getInput() {
        return this.input;
    }

    public int getDuration() {
        return this.properties.get(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost() {
        return this.properties.get(ModRecipeProperty.ENERGY_COST);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.type.getOutputType().toNetwork(buffer, this.output);
        this.properties.toNetwork(buffer);
    }

    @Override
    public boolean matches(IN input) {
        return this.input.test(input);
    }

    @Override
    public int compareInputCount(C other) {
        return other.getInput().getCount() - getInput().getCount();
    }
}
