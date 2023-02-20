package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

/**
 * Multi Input, Multi Output recipe
 */
public abstract class MIMORecipe extends BaseRecipeImpl<MIMORecipeType<?>, List<ItemStack>, MIMORecipe> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final List<ItemStack> output;
    protected final RecipePropertyMap properties;
    
    public MIMORecipe(MIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> output, int duration, double energyCost) {
        this(type, serializer, id, inputs, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }

    public MIMORecipe(MIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> output, RecipePropertyMap properties) {
        super(type, serializer, id);

        this.inputs = inputs;
        this.output = output;
        this.properties = properties;

        RecipeUtil.validateInputList(this.id, "inputs", this.inputs, this.type.inputCount);
        RecipeUtil.validateOutputList(this.id, "outputs", this.type.outputType, this.type.outputCount, this.output);
        this.properties.validate(this.id, this.type.properties);
    }

    public List<? extends RecipeIngredient<ItemStack>> getInputs() {
        return this.inputs;
    }

    public List<ItemStack> getOutput() {
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
    public boolean matches(List<ItemStack> input) {
        return this.inputs.size() == input.size() && EntryStream.zip(this.inputs, input).allMatch(Predicate::test);
    }

    @Override
    public int compareInputCount(MIMORecipe other) {
        return StreamEx.of(this.inputs).mapToInt(RecipeIngredient::getCount).sum()
            - StreamEx.of(other.inputs).mapToInt(RecipeIngredient::getCount).sum();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        ModRecipeIngredientTypes.toNetwork(this.inputs, buffer);
        ModRecipeOutputTypes.toNetwork(this.type.outputType, this.type.outputCount, this.output, buffer);
        this.properties.toNetwork(buffer);
    }
}
