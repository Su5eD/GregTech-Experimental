package mods.gregtechmod.recipe.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeIngredientSerializer extends JsonSerializer<IRecipeIngredient> {
    public static final RecipeIngredientSerializer INSTANCE = new RecipeIngredientSerializer();

    @Override
    public void serialize(IRecipeIngredient value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        int count = value.getCount();

        if (value instanceof RecipeIngredientOre) {
            gen.writeStartObject();
            List<String> ores = ((RecipeIngredientOre) value).asIngredient().getOres();
            if (ores.size() > 1) {
                gen.writeArrayFieldStart("ores");
                ores.forEach(ore -> {
                    try {
                        gen.writeString(ore);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                gen.writeEndArray();
            } else {
                gen.writeStringField("ore", ores.get(0));
            }
            if (count > 1) gen.writeNumberField("count", count);
            gen.writeEndObject();
        } else if (value instanceof RecipeIngredientFluid) {
            gen.writeStartObject();
            List<String> fluids = ((RecipeIngredientFluid) value).getMatchingFluids().stream()
                    .map(Fluid::getName)
                    .collect(Collectors.toList());
            if (fluids.size() > 1) {
                gen.writeArrayFieldStart("fluids");
                fluids.forEach(fluid -> {
                    try {
                        gen.writeString(fluid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                gen.writeEndArray();
            } else {
                gen.writeStringField("fluid", fluids.get(0));
            }
            if (count > 1) gen.writeNumberField("count", count);
            gen.writeEndObject();
        } else {
            gen.writeStartObject();
            List<ItemStack> inputs = value.getMatchingInputs();
            if (inputs.size() > 1) {
                gen.writeArrayFieldStart("items");
                inputs.forEach(stack -> {
                    try {
                        gen.writeObject(stack);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                gen.writeEndArray();
            } else {
                ItemStackSerializer.INSTANCE.serialize(inputs.get(0), gen, count);
            }
            gen.writeEndObject();
        }
    }
}
