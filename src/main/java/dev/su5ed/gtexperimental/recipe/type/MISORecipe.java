package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

/**
 * Multi Input, Single Output recipe
 */
public abstract class MISORecipe<IN, OUT> extends BaseRecipeImpl<MISORecipeType<?, IN, OUT>, List<IN>, MISORecipe<IN, OUT>> {
    protected final List<? extends RecipeIngredient<IN>> inputs;
    protected final OUT output;
    protected final RecipePropertyMap properties;

    public MISORecipe(MISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<IN>> inputs, OUT output, int duration, double energyCost) {
        this(type, serializer, id, inputs, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }
    
    public MISORecipe(MISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<IN>> inputs, OUT output, RecipePropertyMap properties) {
        super(type, serializer, id);

        this.inputs = inputs;
        this.output = output;
        this.properties = properties;

        RecipeUtil.validateInputList(this.id, "inputs", this.inputs, this.type.inputCount);
        this.type.outputType.validate(this.id, "output", this.output, false);
        this.properties.validate(this.id, this.type.properties);
    }

    public List<? extends RecipeIngredient<IN>> getInputs() {
        return this.inputs;
    }

    public OUT getOutput() {
        return this.output;
    }

    public RecipePropertyMap getProperties() {
        return this.properties;
    }

    public int getDuration() {
        return this.properties.get(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost() {
        return this.properties.get(ModRecipeProperty.ENERGY_COST);
    }

    @Override
    public boolean matches(List<IN> input) {
        return this.inputs.size() == input.size() && EntryStream.zip(this.inputs, input).allMatch(Predicate::test);
    }

    @Override
    public int compareInputCount(MISORecipe<IN, OUT> other) {
        return StreamEx.of(this.inputs).mapToInt(RecipeIngredient::getCount).sum()
            - StreamEx.of(other.inputs).mapToInt(RecipeIngredient::getCount).sum();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        ModRecipeIngredientTypes.toNetwork(this.inputs, buffer);
        this.type.outputType.toNetwork(buffer, this.output);
        this.properties.toNetwork(buffer);
    }
}
