package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

@SuppressWarnings("unused")
public class RemainderLessShapelessRecipeFactory implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        json.addProperty("type", "minecraft:crafting_shapeless");
        IRecipe recipe = CraftingHelper.getRecipe(json, context);
        return new RemainderLessShapedRecipe(recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
    }

    public static class RemainderLessShapedRecipe extends ShapelessRecipes {
        public RemainderLessShapedRecipe(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
            super(group, output, ingredients);
        }

        @Override
        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
            return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        }
    }
}
