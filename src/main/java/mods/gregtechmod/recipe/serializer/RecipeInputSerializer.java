package mods.gregtechmod.recipe.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ic2.api.recipe.IRecipeInput;
import ic2.core.recipe.RecipeInputOreDict;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.List;

public class RecipeInputSerializer extends JsonSerializer<IRecipeInput> {

    @Override
    public void serialize(IRecipeInput value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        if (value instanceof RecipeInputOreDict) {
            gen.writeStringField("ore", ((RecipeInputOreDict) value).input);
        }
        else {
            List<ItemStack> stacks = value.getInputs();
            if (stacks.size() > 1) gen.writeObjectField("items", stacks);
        }

        int count = value.getAmount();
        if (count > 1) gen.writeNumberField("count", count);

        gen.writeEndObject();
    }
}
