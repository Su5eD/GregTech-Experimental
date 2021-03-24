package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public class RecipeSimpleSerializer extends RecipeSerializer<IMachineRecipe<IRecipeIngredient, ItemStack>, IRecipeIngredient, ItemStack> {
    public static final RecipeSimpleSerializer INSTANCE = new RecipeSimpleSerializer();

    @Override
    public void serializeInput(IRecipeIngredient input, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("input", input);
    }

    @Override
    public void serializeOutput(ItemStack output, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("output", output);
    }

    @Override
    public void serializeExtraFields(IMachineRecipe<IRecipeIngredient, ItemStack> recipe, JsonGenerator gen, SerializerProvider serializers) {}
}
