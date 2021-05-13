package mods.gregtechmod.recipe.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ItemStackComparator implements Comparator<ItemStack> {
    public static final ItemStackComparator INSTANCE = new ItemStackComparator();

    @Override
    public int compare(ItemStack first, ItemStack second) {
        int diff = first.getItem().getRegistryName().compareTo(second.getItem().getRegistryName());
        if (diff == 0) diff += second.getMetadata() - first.getMetadata();
        if (diff == 0) diff += second.getCount() - first.getCount();
        if (diff == 0) {
            NBTTagCompound firstCompound = first.getTagCompound();
            NBTTagCompound secondCompound = second.getTagCompound();
            if (firstCompound != null && secondCompound != null) diff += compareNBT(firstCompound, secondCompound);
        }

        return diff;
    }

    public static int compareNBT(NBTTagCompound first, NBTTagCompound second) {
        int diff = 0;
        Set<String> firstKeys = first.getKeySet();
        Set<String> secondKeys = second.getKeySet();

        Set<String> toCheck = new HashSet<>();
        toCheck.addAll(firstKeys);
        toCheck.addAll(secondKeys);

        for (String key : toCheck) {
            if (!first.hasKey(key) || !second.hasKey(key)) continue;
            diff += compareNBTBase(first.getTag(key), second.getTag(key));
        }

        return diff;
    }

    public static int compareNBTBase(NBTBase first, NBTBase second) {
        int diff = 0;

        switch (first.getId()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                diff += ((NBTPrimitive) second).getInt() - ((NBTPrimitive) first).getInt();
                break;
            case 7:
                for (byte i : ((NBTTagByteArray) first).getByteArray()) {
                    for (byte j : ((NBTTagByteArray) second).getByteArray()) {
                        diff += i - j;
                    }
                }
                break;
            case 8:
                diff += ((NBTTagString) first).getString().compareTo(((NBTTagString) second).getString());
                break;
            case 9:
                for (NBTBase firstTag : (NBTTagList) first) {
                    for (NBTBase secondTag : (NBTTagList) second) {
                        diff += compareNBTBase(firstTag, secondTag);
                    }
                }
                break;
            case 10:
                diff += compareNBT((NBTTagCompound) first, (NBTTagCompound) second);
                break;
            case 11:
                for (int i : ((NBTTagIntArray) first).getIntArray()) {
                    for (int j : ((NBTTagIntArray) second).getIntArray()) {
                        diff += i - j;
                    }
                }
                break;
        }

        return diff;
    }
}
