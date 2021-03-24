package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class WrenchCraftingRecipeShapelessFactory implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        json.addProperty("type", "minecraft:crafting_shapeless");

        ShapelessRecipes recipe = (ShapelessRecipes) CraftingHelper.getRecipe(json, context);
        return new ToolCraftingRecipeShapeless(recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients(), WrenchCraftingRecipeShapedFactory.getWrenches(), 8);
    }
}
