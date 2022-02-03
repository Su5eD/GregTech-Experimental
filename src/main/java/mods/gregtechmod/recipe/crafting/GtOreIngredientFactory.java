package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mods.gregtechmod.recipe.ingredient.MultiOreIngredient;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import one.util.streamex.StreamEx;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("unused")
public class GtOreIngredientFactory implements IIngredientFactory {
    
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        JsonArray array = JsonUtils.getJsonArray(json, "ores");
        List<String> ores = StreamEx.of(array)
            .map(JsonArray::getAsString)
            .toImmutableList();
        return new MultiOreIngredient(ores);
    }
}
