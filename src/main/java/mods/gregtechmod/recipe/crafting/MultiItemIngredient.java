package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
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
        ResourceLocation location = new ResourceLocation(parts[0]);

        ItemStack stack = ModHandler.getMultiItemOrTEBlock(location, parts[1]);
        return Ingredient.fromStacks(stack);
    }
}
