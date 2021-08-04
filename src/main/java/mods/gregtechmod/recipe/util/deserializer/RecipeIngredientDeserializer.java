package mods.gregtechmod.recipe.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.fuel.FuelIngredientFluidDeserializer;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientDamagedStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.util.ProfileDelegate;
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
        IRecipeIngredient ingredient = RecipeIngredientItemStack.EMPTY;
        int count = node.has("count") ? node.get("count").asInt(1) : 1;

        if (node.has("item")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node, 1);
            ingredient = RecipeIngredientItemStack.create(stack, count);
        } else if (node.has("items")) {
            List<ItemStack> stacks = new ArrayList<>();
            node.get("items").elements().forEachRemaining(item -> stacks.add(ItemStackDeserializer.INSTANCE.deserialize(item, 1)));
            ingredient = RecipeIngredientItemStack.create(stacks, count);
        } else if (node.has("cell")) {
            ItemStack cell = ProfileDelegate.getCell(node.get("cell").asText());
            ingredient = RecipeIngredientItemStack.create(cell, count);
        } else if (node.has("ore")) {
            ingredient = RecipeIngredientOre.create(node.get("ore").asText(), count);
        } else if (node.has("ores")) {
            List<String> ores = new ArrayList<>();
            node.get("ores").elements().forEachRemaining(ore -> ores.add(ore.asText()));
            ingredient = RecipeIngredientOre.create(ores, count);
        } else if (node.has("fluid") || node.has("fluids")) {
            ingredient = node.has("milliBuckets") ? FuelIngredientFluidDeserializer.INSTANCE.deserialize(node) : RecipeIngredientFluidDeserializer.INSTANCE.deserialize(node);
        }
        else if (node.has("damaged")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node, 1);
            ingredient = RecipeIngredientDamagedStack.create(stack, count);
        }

        if (ingredient.isEmpty() && node.has("fallback")) {
            ingredient = deserialize(node.get("fallback"));
        }

        return ingredient;
    }
}
