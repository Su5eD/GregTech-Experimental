package mods.gregtechmod.api.util;

import ic2.api.item.ElectricItem;
import mods.gregtechmod.api.item.IElectricArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.function.BiPredicate;

public class GtUtil {
    public static final Random RANDOM = new Random();

    public static <T, U> BiPredicate<T, U> alwaysTrue() {
        return (a, b) -> true;
    }

    public static String capitalizeString(String aString) {
        if (aString != null && aString.length() > 0) return aString.substring(0, 1).toUpperCase() + aString.substring(1);
        return "";
    }

    public static boolean getFullInvisibility(EntityPlayer player) {
        if (player.isInvisible()) {
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack != ItemStack.EMPTY && stack.getItem() instanceof IElectricArmor && ((IElectricArmor)stack.getItem()).getPerks().contains(ArmorPerk.invisibility_field) && ElectricItem.manager.canUse(stack, 10000)) return true;
            }
        }
        return false;
    }
}
