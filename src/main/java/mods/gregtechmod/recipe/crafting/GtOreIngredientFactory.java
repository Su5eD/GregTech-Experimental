package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mods.gregtechmod.recipe.ingredient.GtOreIngredient;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GtOreIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        List<String> ores = new ArrayList<>();
        JsonArray array = JsonUtils.getJsonArray(json, "ores");
        array.forEach(element -> ores.add(element.getAsString()));
        return new GtOreIngredient(ores);
    }
}
