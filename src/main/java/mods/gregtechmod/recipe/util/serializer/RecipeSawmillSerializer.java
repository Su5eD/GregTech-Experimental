package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.IRecipeSawmill;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;
import java.util.List;

public class RecipeSawmillSerializer extends RecipeSerializer<IRecipeSawmill, IRecipeIngredient, List<ItemStack>> {
    public static final RecipeSawmillSerializer INSTANCE = new RecipeSawmillSerializer();

    public RecipeSawmillSerializer() {
        this.writeDuration = false;
        this.writeEnergyCost = false;
    }

    @Override
    public void serializeInput(IRecipeIngredient input, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("input", input);
    }

    @Override
    public void serializeOutput(List<ItemStack> output, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("output", output);
    }

    @Override
    public void serializeExtraFields(IRecipeSawmill recipe, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        FluidStack fluid = recipe.getRequiredWater();
        if (fluid.amount != Fluid.BUCKET_VOLUME) gen.writeNumberField("water", fluid.amount / 1000);
    }
}
