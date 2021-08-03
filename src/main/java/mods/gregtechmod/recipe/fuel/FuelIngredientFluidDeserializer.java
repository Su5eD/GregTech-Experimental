package mods.gregtechmod.recipe.fuel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import net.minecraftforge.fluids.Fluid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FuelIngredientFluidDeserializer extends JsonDeserializer<IRecipeIngredientFluid> {
    public static final FuelIngredientFluidDeserializer INSTANCE = new FuelIngredientFluidDeserializer();

    @Override
    public IRecipeIngredientFluid deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }
    
    public IRecipeIngredientFluid deserialize(JsonNode node) {
        IRecipeIngredientFluid ingredient = RecipeIngredientFluid.EMPTY;
        int milliBuckets = node.has("milliBuckets") ? node.get("milliBuckets").asInt(Fluid.BUCKET_VOLUME) : Fluid.BUCKET_VOLUME;

        if (node.isTextual()) {
            ingredient = FuelIngredientFluid.fromName(node.asText(), milliBuckets);
        } else if (node.has("fluid")) {
            ingredient = FuelIngredientFluid.fromName(node.get("fluid").asText(), milliBuckets);
        } else if (node.has("fluids")) {
            List<String> names = new ArrayList<>();
            node.get("fluids").elements().forEachRemaining(name -> names.add(name.asText()));
            ingredient = FuelIngredientFluid.fromNames(names, milliBuckets);
        }

        if (ingredient.isEmpty() && node.has("fallback")) {
            ingredient = deserialize(node.get("fallback"));
        }

        return ingredient;
    }
}
