package mods.gregtechmod.recipe.crafting;

import ic2.api.item.ElectricItem;
import ic2.api.item.IC2Items;
import ic2.api.item.IElectricItem;
import ic2.core.util.StackUtil;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.Collection;
import java.util.Collections;

public class ToolCraftingRecipeShaped extends ShapedRecipes {
    private final Collection<ItemStack> tools;
    private final int craftingDamage;

    public ToolCraftingRecipeShaped(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, Collection<ItemStack> tools, int craftingDamage) {
        super(group, width, height, ingredients, result);
        this.tools = tools;
        this.craftingDamage = craftingDamage;
    }
    
    public static IRecipe makeSawingRecipe(ItemStack result, Object... params) {
        return makeRecipe("", ModHandler.adjustWoodSize(result), Collections.singletonList(IC2Items.getItem("chainsaw")), 1, params);
    }
    
    public static IRecipe makeRecipe(String group, ItemStack result, Collection<ItemStack> tools, int craftingDamage, Object... params) {
        CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(params);
        return new ToolCraftingRecipeShaped(group, primer.width, primer.height, primer.input, result, tools, craftingDamage);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return getRemainingItems(inv, this.tools, this.craftingDamage);
    }

    public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv, Collection<ItemStack> tools, int craftingDamage) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (tools.stream()
                    .map(ItemStack::getItem)
                    .anyMatch(item -> StackUtil.checkItemEquality(itemstack, item))) {
                if (itemstack.getItem() instanceof IElectricItem) ElectricItem.manager.use(itemstack, craftingDamage * 1000, null);
                else itemstack.attemptDamageItem(craftingDamage, GtUtil.RANDOM, null);
                list.set(i, itemstack.copy());
            } else list.set(i, ForgeHooks.getContainerItem(itemstack));
        }

        return list;
    }
}
