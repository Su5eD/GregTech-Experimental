package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ic2.api.recipe.MachineRecipe;

import java.io.IOException;
import java.util.List;

public class MachineRecipeSerializer extends JsonSerializer<MachineRecipe> {

    @Override
    public void serialize(MachineRecipe value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("input", value.getInput());
        gen.writeObjectField("output", ((List<?>) value.getOutput()).get(0));

        gen.writeEndObject();
    }
}
