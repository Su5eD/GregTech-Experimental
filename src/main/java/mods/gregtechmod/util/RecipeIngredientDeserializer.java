package mods.gregtechmod.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public class RecipeIngredientDeserializer extends JsonDeserializer<IRecipeIngredient> {
    public static final RecipeIngredientDeserializer INSTANCE = new RecipeIngredientDeserializer();

    @Override
    public IRecipeIngredient deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        if (node.has("item")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node);
            if (!stack.isEmpty()) {
                return new RecipeIngredientItemStack(stack);
            }
        } else if (node.has("ore")) {
            int count = node.has("count") ? node.get("count").asInt(1) : 1;
            return new RecipeIngredientOre(node.get("ore").asText(), count);
        }

        return null;
    }
}
