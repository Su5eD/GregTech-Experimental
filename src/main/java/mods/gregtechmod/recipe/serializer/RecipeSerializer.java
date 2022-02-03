package mods.gregtechmod.recipe.serializer;

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

        serializeInput(value.getInput(), gen);
        serializeOutput(value.getOutput(), gen);
        serializeExtraFields(value, gen);

        if (this.writeDuration) gen.writeNumberField("duration", value.getDuration());
        if (this.writeEnergyCost) gen.writeNumberField("energyCost", value.getEnergyCost());
        gen.writeEndObject();
    }

    public void serializeInput(I input, JsonGenerator gen) throws IOException {
        gen.writeObjectField("input", input);
    }

    public void serializeOutput(O output, JsonGenerator gen) throws IOException {
        gen.writeObjectField("output", output);
    }

    public void serializeExtraFields(R recipe, JsonGenerator gen) throws IOException {}
}
