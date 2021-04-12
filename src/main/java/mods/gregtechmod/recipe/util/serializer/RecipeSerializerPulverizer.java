package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeSerializerPulverizer extends RecipeSerializer<IRecipePulverizer, IRecipeIngredient, List<ItemStack>> {

    public RecipeSerializerPulverizer() {
        super(false, false);
    }

    @Override
    public void serializeExtraFields(IRecipePulverizer recipe, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        int chance = recipe.getChance();
        if (chance != 10) gen.writeNumberField("chance", chance);

        boolean overwrite = recipe.shouldOverwrite();
        if (overwrite) gen.writeBooleanField("overwrite", true);
    }
}
