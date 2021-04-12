package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeLatheSerializer extends RecipeSerializer<IMachineRecipe<IRecipeIngredient, List<ItemStack>>, IRecipeIngredient, List<ItemStack>> {
    public static final RecipeLatheSerializer INSTANCE = new RecipeLatheSerializer();

    @Override
    public void serializeInput(IRecipeIngredient input, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("input", input);
    }

    @Override
    public void serializeOutput(List<ItemStack> output, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("output", output);
    }

    @Override
    public void serializeExtraFields(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe, JsonGenerator gen, SerializerProvider serializers) {}
}
