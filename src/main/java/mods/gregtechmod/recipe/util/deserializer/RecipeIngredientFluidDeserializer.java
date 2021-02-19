package mods.gregtechmod.recipe.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientFluidDeserializer extends JsonDeserializer<IRecipeIngredientFluid> {
    public static final RecipeIngredientFluidDeserializer INSTANCE = new RecipeIngredientFluidDeserializer();

    @Override
    public IRecipeIngredientFluid deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    public IRecipeIngredientFluid deserialize(JsonNode node) {
        IRecipeIngredientFluid ingredient = RecipeIngredientFluid.EMPTY;
        int count = node.has("count") ? node.get("count").asInt(1) : 1;

        if (node.isTextual()) {
            ingredient = RecipeIngredientFluid.fromName(node.asText(), count);
        } else if (node.has("fluid")) {
            ingredient = RecipeIngredientFluid.fromName(node.get("fluid").asText(), count);
        } else if (node.has("fluids")) {
            List<String> names = new ArrayList<>();
            node.get("fluids").elements().forEachRemaining(name -> names.add(name.asText()));
            ingredient = RecipeIngredientFluid.fromNames(names, count);
        }

        if (ingredient.isEmpty() && node.has("fallback")) {
            ingredient = deserialize(node.get("fallback"));
        }

        return ingredient;
    }
}
