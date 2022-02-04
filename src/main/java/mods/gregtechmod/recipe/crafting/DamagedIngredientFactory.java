package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class DamagedIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        String itemName = JsonUtils.getString(json, "item");
        String[] parts = itemName.split(":");
        Item item = ModHandler.getItem(parts[0], parts[1]);

        if (item != null) return DamagedIngredient.fromItem(item);
        throw new JsonSyntaxException("Item " + itemName + " not found");
    }
}
