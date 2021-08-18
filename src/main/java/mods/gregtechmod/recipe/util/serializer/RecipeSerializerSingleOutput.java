package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeSerializerSingleOutput<R extends IMachineRecipe<RI, List<ItemStack>>, RI> extends RecipeSerializer<R, RI, List<ItemStack>> {

    @Override
    public void serializeOutput(List<ItemStack> output, JsonGenerator gen) throws IOException {
        gen.writeObjectField("output", output.get(0));
    }
}
