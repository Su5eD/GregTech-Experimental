package mods.gregtechmod.recipe.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeSerializerSawmill extends RecipeSerializer<IRecipeUniversal<List<IRecipeIngredient>>, List<IRecipeIngredient>, List<ItemStack>> {

    public RecipeSerializerSawmill() {
        super(false, false);
    }

    @Override
    public void serializeInput(List<IRecipeIngredient> input, JsonGenerator gen) throws IOException {
        gen.writeObjectField("input", input.get(0));
    }

    @Override
    public void serializeExtraFields(IRecipeUniversal<List<IRecipeIngredient>> recipe, JsonGenerator gen) throws IOException {
        gen.writeNumberField("water", recipe.getInput().get(1).getCount());
    }
}
