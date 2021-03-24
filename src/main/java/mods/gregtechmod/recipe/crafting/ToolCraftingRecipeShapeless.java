package mods.gregtechmod.recipe.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

import java.util.Collection;

public class ToolCraftingRecipeShapeless extends ShapelessRecipes {
    private final Collection<ItemStack> tools;
    private final int craftingDamage;

    public ToolCraftingRecipeShapeless(String group, ItemStack output, NonNullList<Ingredient> ingredients, Collection<ItemStack> tools, int craftingDamage) {
        super(group, output, ingredients);
        this.tools = tools;
        this.craftingDamage = craftingDamage;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ToolCraftingRecipeShaped.getRemainingItems(inv, this.tools, this.craftingDamage);
    }
}
