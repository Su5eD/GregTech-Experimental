package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class MultiIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        JsonArray array = JsonUtils.getJsonArray(json, "items");
        int size = array.size();
        ItemStack[] stacks = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            stacks[i] = CraftingHelper.getIngredient(array.get(i).getAsJsonObject(), context).getMatchingStacks()[0]; //Allows compatibility with custom ingredient types
        }
        return Ingredient.fromStacks(stacks);
    }
}
