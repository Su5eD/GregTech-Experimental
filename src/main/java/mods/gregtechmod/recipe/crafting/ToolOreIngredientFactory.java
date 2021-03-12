package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class ToolOreIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        String ore = JsonUtils.getString(json, "ore");
        int damage = JsonUtils.getInt(json, "damage");
        return new ToolOreIngredient(ore, damage);
    }
}
