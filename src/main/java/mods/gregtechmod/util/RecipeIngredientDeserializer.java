package mods.gregtechmod.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientDeserializer extends JsonDeserializer<IRecipeIngredient> {
    public static final RecipeIngredientDeserializer INSTANCE = new RecipeIngredientDeserializer();

    @Override
    public IRecipeIngredient deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    public IRecipeIngredient deserialize(JsonNode node) {
        IRecipeIngredient ingredient = null;
        int count = node.has("count") ? node.get("count").asInt(1) : 1;

        if (node.has("item")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node);
            ingredient = RecipeIngredientItemStack.create(stack, count);
        } else if (node.has("items")) {
            List<ItemStack> stacks = new ArrayList<>();
            node.get("items").elements().forEachRemaining(item -> stacks.add(ItemStackDeserializer.INSTANCE.deserialize(item)));
            ingredient = RecipeIngredientItemStack.create(stacks, count);
        } else if (node.has("ore")) {
            ingredient = RecipeIngredientOre.create(node.get("ore").asText(), count);
        } else if (node.has("ores")) {
            List<String> ores = new ArrayList<>();
            node.get("ores").elements().forEachRemaining(ore -> ores.add(ore.asText()));
            ingredient = RecipeIngredientOre.create(ores, count);
        } else if (node.has("fluid")) {
            ingredient = RecipeIngredientFluid.fromName(node.get("fluid").asText(), count);
        } else if (node.has("fluids")) {
            List<String> names = new ArrayList<>();
            node.get("fluids").elements().forEachRemaining(name -> names.add(name.asText()));
            ingredient = RecipeIngredientFluid.fromNames(names, count);
        }

        if (ingredient == null && node.has("fallback")) {
            ingredient = deserialize(node.get("fallback"));
        }

        return ingredient;
    }
}
