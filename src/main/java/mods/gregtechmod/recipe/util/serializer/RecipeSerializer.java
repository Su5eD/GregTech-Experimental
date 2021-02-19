package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;

import java.io.IOException;

public abstract class RecipeSerializer<R extends IGtMachineRecipe<I, O>, I, O> extends JsonSerializer<R> {
    protected boolean writeDuration = true;
    protected boolean writeEnergyCost = true;

    @Override
    public void serialize(R value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        serializeInput(value.getInput(), gen, serializers);
        serializeOutput(value.getOutput(), gen, serializers);
        serializeExtraFields(value, gen, serializers);

        if (writeDuration) gen.writeNumberField("duration", value.getDuration());
        if (writeEnergyCost) gen.writeNumberField("energyCost", value.getEnergyCost());
        gen.writeEndObject();
    }

    public abstract void serializeInput(I input, JsonGenerator gen, SerializerProvider serializers) throws IOException;

    public abstract void serializeOutput(O output, JsonGenerator gen, SerializerProvider serializers) throws IOException;

    public abstract void serializeExtraFields(R recipe, JsonGenerator gen, SerializerProvider serializers) throws IOException;
}
