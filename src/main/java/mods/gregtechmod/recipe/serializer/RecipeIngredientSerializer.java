package mods.gregtechmod.recipe.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.util.List;

public class RecipeIngredientSerializer extends JsonSerializer<IRecipeIngredient> {

    @Override
    public void serialize(IRecipeIngredient value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        int count = value.getCount();

        if (value instanceof RecipeIngredientOre) {
            List<String> ores = ((RecipeIngredientOre) value).asIngredient().getOres();
            if (ores.size() > 1) {
                gen.writeObjectField("ores", ores);
            }
            else {
                gen.writeStringField("ore", ores.get(0));
            }
            if (count > 1) gen.writeNumberField("count", count);
        }
        else if (value instanceof RecipeIngredientFluid) {
            List<String> fluids = StreamEx.of(((RecipeIngredientFluid) value).getMatchingFluids())
                .map(Fluid::getName)
                .toImmutableList();
            if (fluids.size() > 1) {
                gen.writeObjectField("fluids", fluids);
            }
            else {
                gen.writeStringField("fluid", fluids.get(0));
            }
            if (count > 1) gen.writeNumberField("count", count);
        }
        else {
            List<ItemStack> inputs = value.getMatchingInputs();
            if (inputs.size() > 1) {
                gen.writeObjectField("items", inputs);
            }
            else {
                ItemStackSerializer.INSTANCE.serialize(inputs.get(0), gen, count);
            }
        }

        gen.writeEndObject();
    }
}
