package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import ic2.api.item.IC2Items;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.LazyValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("unused")
public class WrenchCraftingRecipeShapedFactory implements IRecipeFactory {
    public static final LazyValue<Collection<ItemStack>> WRENCHES = new LazyValue<>(() -> {
        Collection<ItemStack> wrenches = new HashSet<>();
        wrenches.add(IC2Items.getItem("wrench"));
        wrenches.add(IC2Items.getItem("electric_wrench"));
        if (!GregTechMod.classic) wrenches.add(IC2Items.getItem("wrench_new"));
        return wrenches;
    });

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        json.addProperty("type", "minecraft:crafting_shaped");

        ShapedRecipes recipe = (ShapedRecipes) CraftingHelper.getRecipe(json, context);
        return new ToolCraftingRecipeShaped(recipe.getGroup(), recipe.recipeWidth, recipe.recipeHeight, recipe.getIngredients(), recipe.getRecipeOutput(), WRENCHES.get(), 8);
    }
}
