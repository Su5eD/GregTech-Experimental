package mods.gregtechmod.recipe.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.core.GregTechMod;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;

public class FluidStackDeserializer extends JsonDeserializer<FluidStack> {
    public static final FluidStackDeserializer INSTANCE = new FluidStackDeserializer();

    @Override
    public FluidStack deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        return deserialize(node, getAmount(node));
    }

    public FluidStack deserialize(JsonNode node, int count) {
        Fluid fluid;
        boolean hasFallback = node.has("fallback");

        if (node.has("fluid")) {
            String name = node.get("fluid").asText();
            fluid = FluidRegistry.getFluid(name);
            if (fluid == null) {
                if (!hasFallback) {
                    GregTechMod.logger.warn("Unable to find fluid "+name);
                } else {
                    JsonNode fallback = node.get("fallback");
                    return deserialize(fallback, getAmount(fallback));
                }
            } else return new FluidStack(fluid, count);
        }

        return null;
    }

    private static int getAmount(JsonNode node) {
        return node.has("amount") ? node.get("amount").asInt(1) : 1;
    }
}
