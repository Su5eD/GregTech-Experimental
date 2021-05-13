package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.IMachineRecipe;

import java.io.IOException;

public class RecipeSerializer<R extends IMachineRecipe<I, O>, I, O> extends JsonSerializer<R> {
    protected final boolean writeDuration;
    protected final boolean writeEnergyCost;

    public RecipeSerializer() {
        this(true, true);
    }

    public RecipeSerializer(boolean writeDuration, boolean writeEnergyCost) {
        this.writeDuration = writeDuration;
        this.writeEnergyCost = writeEnergyCost;
    }

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

    public void serializeInput(I input, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("input", input);
    }

    public void serializeOutput(O output, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObjectField("output", output);
    }

    public void serializeExtraFields(R recipe, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
}
