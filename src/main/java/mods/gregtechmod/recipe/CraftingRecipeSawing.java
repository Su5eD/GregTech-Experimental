package mods.gregtechmod.recipe;

import ic2.api.item.ElectricItem;
import ic2.api.item.IC2Items;
import ic2.core.util.StackUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;

public class CraftingRecipeSawing extends ShapedRecipes {

    public CraftingRecipeSawing(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(group, width, height, ingredients, result);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        Item chainsaw = IC2Items.getItem("chainsaw").getItem();
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (StackUtil.checkItemEquality(itemstack, chainsaw)) {
                ElectricItem.manager.use(itemstack, 1000, null);
                nonnulllist.set(i, itemstack.copy());
            } else nonnulllist.set(i, ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }
}
