package mods.gregtechmod.api.util;

import ic2.api.item.ElectricItem;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.item.IElectricArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.translation.I18n;

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

    public static ItemStack getWrittenBook(String name, String author, int pages, int ordinal) {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        stack.setTagInfo("title", new NBTTagString(GtUtil.translate(Reference.MODID+".book."+name+".name")));
        stack.setTagInfo("author", new NBTTagString(author));
        NBTTagList tagList = new NBTTagList();
        byte i;
        for (i = 0; i < pages; i = (byte)(i + 1)) {
            String page = '\"'+GtUtil.translate(Reference.MODID+".book." + name + ".page" + ((i < 10) ? ("0" + i) : i))+'\"';
            if (i < 48) {
                if (page.length() < 256) {
                    tagList.appendTag(new NBTTagString(page));
                } else {
                    GregTechAPI.logger.warn("WARNING: String for written book too long! -> " + page);
                }
            } else {
                GregTechAPI.logger.warn("WARNING: Too many pages for written book! -> " + name);
                break;
            }
        }
        tagList.appendTag(new NBTTagString("\"Credits to " + author + " for writing this Book. This was Book Nr. " + (ordinal+1) + " at its creation. Gotta get 'em all!\""));
        stack.setTagInfo("pages", tagList);
        return stack;
    }

    public static boolean damageStack(EntityPlayer player, ItemStack stack, int damage) {
        if (stack.isItemStackDamageable())
        {
            if (!player.capabilities.isCreativeMode) {
                if (stack.attemptDamageItem(damage, player.getRNG(), player instanceof EntityPlayerMP ? (EntityPlayerMP)player : null))
                {
                    if (stack.getItem().hasContainerItem(stack)) {
                        ItemStack containerStack = stack.getItem().getContainerItem(stack);
                        if (containerStack != ItemStack.EMPTY) {
                            player.setHeldItem(player.getActiveHand(), containerStack.copy());
                        } else {
                            player.renderBrokenItemStack(stack);
                            stack.shrink(1);
                            player.addStat(StatList.getObjectBreakStats(stack.getItem()));
                            stack.setItemDamage(0);
                        }
                    }
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static String translate(String key) {
        return I18n.translateToLocal(key);
    }
}
