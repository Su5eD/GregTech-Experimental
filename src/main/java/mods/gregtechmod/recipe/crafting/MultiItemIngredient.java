package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import ic2.core.ref.IMultiItem;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class MultiItemIngredient implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        String rawName = JsonUtils.getString(json, "item");
        String[] parts = rawName.split("#");
        String[] name = parts[0].split(":");
        if (name[0].equals("ic2")) {
            return Ingredient.fromStacks(ModHandler.getIC2ItemSafely(name[1], parts[1]));
        } else {
            Item item = ModHandler.getItem(name[0], name[1]);
            if (item instanceof IMultiItem<?>) {
                ItemStack stack = ((IMultiItem<?>) item).getItemStack(parts[1]);
                return Ingredient.fromStacks(stack);
            }
        }
        throw new JsonSyntaxException("IMultiItem "+parts[0]+" not found");
    }
}
