package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import ic2.api.item.IC2Items;
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
        ItemStack fluidCell = IC2Items.getItem("fluid_cell", fluidName);
        return Ingredient.fromStacks(fluidCell);
    }
}
