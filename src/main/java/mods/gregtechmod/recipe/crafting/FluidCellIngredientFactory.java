package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class FluidCellIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        String fluidName = JsonUtils.getString(json, "fluid");
        ItemStack fluidCell = ProfileDelegate.getCell(fluidName);
        return Ingredient.fromStacks(fluidCell);
    }
}
