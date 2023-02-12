package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import mods.gregtechmod.util.LazyValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

@SuppressWarnings("unused")
public class WrenchCraftingRecipeShapedFactory implements IRecipeFactory {
    public static final LazyValue<Collection<ItemStack>> WRENCHES = new LazyValue<>(() -> OreDictionary.getOres("craftingToolWrench"));

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        json.addProperty("type", "minecraft:crafting_shaped");

        ShapedRecipes recipe = (ShapedRecipes) CraftingHelper.getRecipe(json, context);
        return new ToolCraftingRecipeShaped(recipe.getGroup(), recipe.recipeWidth, recipe.recipeHeight, recipe.getIngredients(), recipe.getRecipeOutput(), WRENCHES.get(), 8);
    }
}
