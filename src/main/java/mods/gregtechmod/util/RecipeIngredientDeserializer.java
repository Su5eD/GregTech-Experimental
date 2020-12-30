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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipeIngredientDeserializer extends JsonDeserializer<IRecipeIngredient> {
    public static final RecipeIngredientDeserializer INSTANCE = new RecipeIngredientDeserializer();

    @Override
    public IRecipeIngredient deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        if (node.has("item")) {
            ItemStack stack = ItemStackDeserializer.INSTANCE.deserialize(node);
            if (!stack.isEmpty()) {
                return RecipeIngredientItemStack.create(stack);
            }
        } else if (node.has("ore")) {
            int count = node.has("count") ? node.get("count").asInt(1) : 1;
            return RecipeIngredientOre.create(node.get("ore").asText(), count);
        } else if (node.has("fluid")) {
            int amount = node.has("count") ? node.get("count").asInt(1000) : 1000;
            JsonNode fluid = node.get("fluid");
            List<String> names = new ArrayList<>();
            if (fluid.isArray()) fluid.elements().forEachRemaining(name -> names.add(name.asText()));
            else names.add(fluid.asText());
            List<Fluid> fluids = names.stream()
                    .map(FluidRegistry::getFluid)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return new RecipeIngredientFluid(amount, fluids);
        }

        return null;
    }
}
