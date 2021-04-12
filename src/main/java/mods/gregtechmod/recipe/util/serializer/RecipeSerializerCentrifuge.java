package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeSerializerCentrifuge extends RecipeSerializer<IRecipeCellular, IRecipeIngredient, List<ItemStack>> {

    public RecipeSerializerCentrifuge() {
        super(true, false);
    }

    @Override
    public void serializeExtraFields(IRecipeCellular recipe, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumberField("cells", recipe.getCells());

        CellType cellType = recipe.getCellType();
        if (cellType != CellType.CELL) gen.writeObjectField("cellType", recipe.getCellType());
    }
}
