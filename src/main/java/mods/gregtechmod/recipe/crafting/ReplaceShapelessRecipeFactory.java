package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

@SuppressWarnings("unused")
public class ReplaceShapelessRecipeFactory implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        json.addProperty("type", "minecraft:crafting_shapeless");

        IRecipe recipe = CraftingHelper.getRecipe(json, context);
        ModHandler.removeCraftingRecipe(recipe.getRecipeOutput());
        AdvancementRecipeFixer.REPLACED_RECIPES.add(recipe);

        return recipe;
    }
}
