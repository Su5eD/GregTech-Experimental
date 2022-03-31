package mods.gregtechmod.recipe.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientDamagedStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.util.List;

public class RecipeIngredientDeserializer extends JsonDeserializer<IRecipeIngredient> {
    public static final RecipeIngredientDeserializer INSTANCE = new RecipeIngredientDeserializer();

    @Override
    public IRecipeIngredient deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    public IRecipeIngredient deserialize(JsonNode node) {
        IRecipeIngredient ingredient;
        int count = node.has("count") ? node.get("count").asInt(1) : 1;

        if (node.has("item")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node, 1);
            ingredient = RecipeIngredientItemStack.create(stack, count);
        }
        else if (node.has("items")) {
            List<ItemStack> stacks = StreamEx.of(node.get("items").iterator())
                .map(json -> ItemStackDeserializer.INSTANCE.deserialize(json, 1))
                .toImmutableList();
            ingredient = RecipeIngredientItemStack.create(stacks, count);
        }
        else if (node.has("cell")) {
            ItemStack cell = ProfileDelegate.getCell(node.get("cell").asText());
            ingredient = RecipeIngredientItemStack.create(cell, count);
        }
        else if (node.has("ore")) {
            ingredient = RecipeIngredientOre.create(node.get("ore").asText(), count);
        }
        else if (node.has("ores")) {
            List<String> ores = StreamEx.of(node.get("ores").iterator())
                .map(JsonNode::asText)
                .toImmutableList();
            ingredient = RecipeIngredientOre.create(ores, count);
        }
        else if (node.has("fluid") || node.has("fluids")) {
            ingredient = node.has("milliBuckets") ? FuelIngredientFluidDeserializer.INSTANCE.deserialize(node) : RecipeIngredientFluidDeserializer.INSTANCE.deserialize(node);
        }
        else if (node.has("damaged")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node, 1);
            ingredient = RecipeIngredientDamagedStack.create(stack, count);
        }
        else ingredient = RecipeIngredientItemStack.EMPTY;

        return ingredient.isEmpty() && node.has("fallback") ? deserialize(node.get("fallback")) : ingredient;
    }
}
