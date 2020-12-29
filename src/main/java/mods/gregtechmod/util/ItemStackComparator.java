package mods.gregtechmod.util;

import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class ItemStackComparator implements Comparator<ItemStack> {
    public static final ItemStackComparator INSTANCE = new ItemStackComparator();

    @Override
    public int compare(ItemStack first, ItemStack second) {
        int nameDiff = first.getItem().getRegistryName().compareTo(second.getItem().getRegistryName());
        int countDiff = second.getCount() - first.getCount();
        int metaDiff = second.getMetadata() - first.getMetadata();

        int total = nameDiff;
        if (total == 0) total += metaDiff;
        if (total == 0) total += countDiff;

        return total;
    }
}
