package mods.gregtechmod.recipe.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.recipe.fuel.FuelIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import net.minecraftforge.fluids.Fluid;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.util.List;

public class FuelIngredientFluidDeserializer extends JsonDeserializer<IRecipeIngredientFluid> {
    public static final FuelIngredientFluidDeserializer INSTANCE = new FuelIngredientFluidDeserializer();

    @Override
    public IRecipeIngredientFluid deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    public IRecipeIngredientFluid deserialize(JsonNode node) {
        IRecipeIngredientFluid ingredient;
        int milliBuckets = node.has("milliBuckets") ? node.get("milliBuckets").asInt(Fluid.BUCKET_VOLUME) : Fluid.BUCKET_VOLUME;

        if (node.isTextual()) {
            ingredient = FuelIngredientFluid.fromName(node.asText(), milliBuckets);
        }
        else if (node.has("fluid")) {
            ingredient = FuelIngredientFluid.fromName(node.get("fluid").asText(), milliBuckets);
        }
        else if (node.has("fluids")) {
            List<String> names = StreamEx.of(node.get("fluids").iterator())
                .map(JsonNode::asText)
                .toImmutableList();

            ingredient = FuelIngredientFluid.fromNames(names, milliBuckets);
        }
        else ingredient = RecipeIngredientFluid.EMPTY;

        return ingredient.isEmpty() && node.has("fallback") ? deserialize(node.get("fallback")) : ingredient;
    }
}
