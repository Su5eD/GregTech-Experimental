package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeDualInputSerializer extends RecipeSerializer<IMachineRecipe<List<IRecipeIngredient>, ItemStack>, List<IRecipeIngredient>, ItemStack> {
    public static final RecipeDualInputSerializer INSTANCE = new RecipeDualInputSerializer();

    @Override
    public void serializeInput(List<IRecipeIngredient> input, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeArrayFieldStart("input");
        input.forEach(ingredient -> {
            try {
                gen.writeObject(ingredient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        gen.writeEndArray();
    }

    @Override
    public void serializeOutput(ItemStack output, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("output", output);
    }

    @Override
    public void serializeExtraFields(IMachineRecipe<List<IRecipeIngredient>, ItemStack> recipe, JsonGenerator gen, SerializerProvider serializers) {}
}
