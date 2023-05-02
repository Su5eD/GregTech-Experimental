package mods.gregtechmod.recipe.crafting;

import com.google.common.collect.Lists;
import mods.gregtechmod.util.IRepairableCraftingItem;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class RecipeRepairCraftingItem extends RecipeRepairItem {

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return getMatchingList(inv).size() == 2;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        List<ItemStack> list = getMatchingList(inv);

        if (list.size() == 2) {
            ItemStack first = list.get(0);
            ItemStack second = list.get(1);

            if (first.getItem() == second.getItem() && first.getCount() == 1 && second.getCount() == 1 && accepts(first)) {
                // FORGE: Make itemstack sensitive // Item item = itemstack2.getItem();
                int j = first.getMaxDamage() - first.getItemDamage();
                int k = first.getMaxDamage() - second.getItemDamage();
                int l = j + k + first.getMaxDamage() * 5 / 100;
                int i1 = Math.max(0, first.getMaxDamage() - l);
                return new ItemStack(first.getItem(), 1, i1);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    private List<ItemStack> getMatchingList(InventoryCrafting inv) {
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {
                list.add(stack);

                if (list.size() > 1) {
                    ItemStack firstStack = list.get(0);
                    if (stack.getItem() != firstStack.getItem() || firstStack.getCount() != 1 || stack.getCount() != 1 || !accepts(firstStack)) {
                        return Collections.emptyList();
                    }
                }
            }
        }

        return list;
    }

    private boolean accepts(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IRepairableCraftingItem && ((IRepairableCraftingItem) item).isCraftingRepairable();
    }
}
