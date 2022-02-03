package mods.gregtechmod.recipe.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.RecipePulverizer;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeSerializerPulverizer extends RecipeSerializer<IRecipePulverizer, IRecipeIngredient, List<ItemStack>> {

    public RecipeSerializerPulverizer() {
        super(false, false);
    }

    @Override
    public void serializeExtraFields(IRecipePulverizer recipe, JsonGenerator gen) throws IOException {
        int chance = recipe.getChance();
        if (chance != RecipePulverizer.DEFAULT_CHANCE) gen.writeNumberField("chance", chance);

        boolean overwrite = recipe.shouldOverwrite();
        if (overwrite) gen.writeBooleanField("overwrite", true);
    }
}
