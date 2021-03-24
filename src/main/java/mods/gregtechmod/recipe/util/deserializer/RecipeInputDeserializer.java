package mods.gregtechmod.recipe.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public class RecipeInputDeserializer extends JsonDeserializer<IRecipeInput> {
    public static final RecipeInputDeserializer INSTANCE = new RecipeInputDeserializer();

    @Override
    public IRecipeInput deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        IRecipeInput input = null;
        int count = node.has("count") ? node.get("count").asInt(1) : 1;

        if (node.has("item")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node, 1);
            input = Recipes.inputFactory.forStack(stack, count);
        } else if (node.has("ore")) {
            input = Recipes.inputFactory.forOreDict(node.get("ore").asText(), count);
        }

        return input;
    }
}
