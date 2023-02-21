package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
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
public class MIMORecipe extends BaseRecipeImpl<MIMORecipeType<?>, List<ItemStack>, List<ItemStack>, MIMORecipe> {
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;

    public static MIMORecipe canningMachine(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new MIMORecipe(ModRecipeTypes.CANNING_MACHINE.get(), ModRecipeSerializers.CANNING_MACHINE.get(), id, inputs, outputs, properties);
    }

    public static MIMORecipe blastFurnace(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, RecipePropertyMap properties) {
        return new MIMORecipe(ModRecipeTypes.BLAST_FURNACE.get(), ModRecipeSerializers.BLAST_FURNACE.get(), id, inputs, outputs, properties.withTransient(b -> b.energyCost(128)));
    }

    public MIMORecipe(MIMORecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> output, RecipePropertyMap properties) {
        super(type, serializer, id, output, properties);

        this.inputs = inputs;

        RecipeUtil.validateInputList(this.id, "inputs", this.inputs, this.type.inputCount);
        this.type.outputType.validate(this.id, "outputs", this.output, false);
    }

    public List<? extends RecipeIngredient<ItemStack>> getInputs() {
        return this.inputs;
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
        this.type.outputType.toNetwork(buffer, this.output);
        this.properties.toNetwork(buffer);
    }
}
