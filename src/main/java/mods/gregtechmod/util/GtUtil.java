package mods.gregtechmod.util;

import com.mojang.authlib.GameProfile;
import ic2.api.item.ElectricItem;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.item.upgrade.ItemUpgradeModule;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.item.IElectricArmor;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.util.ArmorPerk;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GtUtil {
    public static final Random RANDOM = new Random();
    public static final Supplier<String> NULL_SUPPLIER = () -> null;
    private static final DecimalFormat INT_FORMAT = new DecimalFormat("#,###,###,##0");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###,###,##0.00");
    private static FileSystem modFile;

    public static FileSystem getModFile() {
        if (modFile == null) {
            try {
                File file = Loader.instance().activeModContainer().getSource();
                modFile = FileSystems.newFileSystem(file.toPath(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return modFile;
    }

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
                if (!stack.isEmpty() && stack.getItem() instanceof IElectricArmor && ((IElectricArmor)stack.getItem()).getPerks().contains(ArmorPerk.INVISIBILITY_FIELD) && ElectricItem.manager.canUse(stack, 10000)) return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public static <T> List<T> nonNullList(T... elements) {
        return Stream.of(elements)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static List<ItemStack> nonEmptyList(ItemStack... elements) {
        return Stream.of(elements)
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
    }

    public static List<ItemStack> copyList(List<ItemStack> list) {
        return list.stream()
                .map(ItemStack::copy)
                .collect(Collectors.toList());
    }

    public static ItemStack getWrittenBook(String name, String author, int pages, int ordinal) {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        stack.setTagInfo("title", new NBTTagString(GtUtil.translate("book."+name+".name")));
        stack.setTagInfo("author", new NBTTagString(author));
        NBTTagList tagList = new NBTTagList();
        byte i;
        for (i = 0; i < pages; i = (byte)(i + 1)) {
            String page = '\"'+GtUtil.translate("book." + name + ".page" + (i < 10 ? "0" + i : i))+'\"';
            if (i < 48) {
                if (page.length() < 256) {
                    tagList.appendTag(new NBTTagString(page));
                } else {
                    GregTechMod.logger.warn("WARNING: String for written book too long! -> " + page);
                }
            } else {
                GregTechMod.logger.warn("WARNING: Too many pages for written book! -> " + name);
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
                        if (!containerStack.isEmpty()) {
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

    public static String translateScan(String key, Object... parameters) {
        return I18n.format(Reference.MODID+".scan."+key, parameters);
    }

    public static String translateTeBlockDescription(String key) {
        return translate("teblock."+key+".description");
    }

    public static String translateInfo(String key, Object... parameters) {
        return translate("info."+key, parameters);
    }

    public static String translateGenericDescription(String key, Object... parameters) {
        return translateGeneric(key+".description", parameters);
    }

    public static String translateGeneric(String key, Object... parameters) {
        return translate("generic."+key, parameters);
    }

    public static String translateItemDescription(String key, Object... parameters) {
        return translateItem(key+".description", parameters);
    }

    public static String translateItem(String key, Object... parameters) {
        return translate("item."+key, parameters);
    }

    public static String translate(String key, Object... parameters) {
        return I18n.format(Reference.MODID+"."+key, parameters);
    }

    public static double getTransferLimit(int tier) {
        return Math.pow(2, tier) * 128;
    }

    public static void damageEntity(EntityLivingBase entity, EntityLivingBase attacker, float damage) {
        int oldHurtResistanceTime = entity.hurtResistantTime;
        entity.hurtResistantTime = 0;
        entity.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
        entity.hurtResistantTime = oldHurtResistanceTime;
    }

    public static List<ItemStack> correctStacksize(List<ItemStack> list) {
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            int maxSize = stack.getMaxStackSize();
            if (stack.getCount() > maxSize) {
                list.remove(i);
                int cycles = stack.getCount() / maxSize + 1;
                for (int j = 0; j < cycles; j++) {
                    list.add(stack.splitStack(maxSize));
                }
            }
        }
        return list;
    }

    public static boolean stackEquals(ItemStack first, ItemStack second) {
        if (first.isEmpty() || second.isEmpty()) return false;

        return first.getItem() == second.getItem()
                && (first.getMetadata() == OreDictionary.WILDCARD_VALUE || first.getMetadata() == second.getMetadata())
                && StackUtil.checkNbtEquality(first.getTagCompound(), second.getTagCompound());
    }

    public static ItemStack copyWithMeta(ItemStack stack, int meta) {
        ItemStack ret = stack.copy();
        ret.setItemDamage(meta);
        return stack;
    }

    public static <T> Collection<T> mergeCollection(Collection<T> first, Collection<T> second) {
        return Stream.of(first, second)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static String formatNumber(int num) {
        return INT_FORMAT.format(num);
    }

    public static String formatNumber(double num) {
        if (num % 1 == 0) return INT_FORMAT.format(num);
        else return DECIMAL_FORMAT.format(num);
    }

    public static ItemStack getFluidContainer(ItemStack stack, Fluid fluid) {
        ItemStack container = stack.copy();
        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(container);
        if (fluidHandler != null) {
            fluidHandler.fill(new FluidStack(fluid, Integer.MAX_VALUE), true);
            return container;
        }

        return ItemStack.EMPTY;
    }

    public static boolean checkAccess(IUpgradableMachine machine, GameProfile owner, GameProfile playerProfile) {
        if (!machine.isPrivate() || owner == null) return true;
        return owner.equals(playerProfile);
    }

    public static void sendMessage(EntityPlayer player, String message, Object... args) {
        if (!player.world.isRemote) player.sendMessage(new TextComponentTranslation(message, args));
    }

    public static IC2UpgradeType getUpgradeType(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IUpgradeItem) {
            ItemUpgradeModule.UpgradeType upgradeType = ItemUpgradeModule.UpgradeType.values()[stack.getMetadata()];
            for (IC2UpgradeType type : IC2UpgradeType.values()) {
                if (type.itemType.equals(upgradeType.name())) return type;
            }
        }
        return null;
    }
}
