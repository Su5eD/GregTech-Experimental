package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import ic2.api.item.IC2Items;
import ic2.core.IC2;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.Collection;
import java.util.HashSet;

public class WrenchCraftingRecipeShapedFactory implements IRecipeFactory {
    private static Collection<ItemStack> wrenches = null;

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        json.addProperty("type", "minecraft:crafting_shaped");

        ShapedRecipes recipe = (ShapedRecipes) CraftingHelper.getRecipe(json, context);
        return new ToolCraftingRecipeShaped(recipe.getGroup(), recipe.recipeWidth, recipe.recipeHeight, recipe.getIngredients(), recipe.getRecipeOutput(), getWrenches(), 8);
    }

    public static Collection<ItemStack> getWrenches() {
        if (wrenches == null) {
            wrenches = new HashSet<>();
            wrenches.add(IC2Items.getItem("wrench"));
            if (!IC2.version.isClassic()) wrenches.add(IC2Items.getItem("wrench_new"));
            wrenches.add(IC2Items.getItem("electric_wrench"));
        }
        return wrenches;
    }
}
