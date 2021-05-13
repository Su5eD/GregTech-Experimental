package mods.gregtechmod.util;

import ic2.api.item.IC2Items;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.stream.Collectors;

public class OreDictUnificator {
    private static final HashMap<String, ItemStack> name2OreMap = new HashMap<>();
    private static final HashMap<ItemStack, String> item2OreMap = new HashMap<>();
    private static final ArrayList<ItemStack> sBlackList = new ArrayList<>();
    private static ItemStack emptyCell = null;

    public static void addToBlacklist(ItemStack stack) {
        sBlackList.add(stack);
    }

    public static void add(String name, Block block) {
        add(name, new ItemStack(block));
    }

    public static void add(String name, Item item) {
        add(name, new ItemStack(item));
    }

    public static void add(String name, ItemStack stack) {
        set(name, stack, false);
    }

    public static void set(String name, ItemStack stack) {
        set(name, stack, true);
    }

    public static void set(String name, ItemStack stack, boolean overwrite) {
        if (name == null || name.isEmpty() || stack.isEmpty() || stack.getItemDamage() < 0) return;
        stack = stack.copy().splitStack(1);
        addAssociation(name, stack);
        if (!name2OreMap.containsKey(name)) {
            name2OreMap.put(name, stack);
        } else {
            if (overwrite && Arrays.asList(GregTechConfig.GENERAL.specialUnificationTargets).contains(getStackConfigName(stack))) {
                name2OreMap.remove(name);
                name2OreMap.put(name, stack);
            }
        }
        registerOre(name, stack);
    }

    public static void override(String name, ItemStack stack) {
        if (name == null || name.isEmpty() || name.startsWith("itemDust") || stack.isEmpty() || stack.getItemDamage() < 0) return;

        if (stack.getDisplayName().isEmpty() || Arrays.asList(GregTechConfig.GENERAL.specialUnificationTargets).contains(getStackConfigName(stack))) set(name, stack);
    }

    public static ItemStack getUnifiedOre(String name) {
        ItemStack stack = name2OreMap.get(name);
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public static ItemStack getFirstOre(String name) {
        return getFirstOre(name, 1);
    }

    public static ItemStack getFirstOre(String name, int amount) {
        if (name == null || name.isEmpty()) return null;
        if (name2OreMap.containsKey(name)) return get(name, ItemStack.EMPTY, amount);

        ItemStack stack = ItemStack.EMPTY;
        List<ItemStack> ores = OreDictionary.getOres(name);
        if (ores.size() > 0) stack = ores.get(0).copy();
        if (!stack.isEmpty()) stack.setCount(amount);

        return stack;
    }

    public static ItemStack getFirstCapsulatedOre(String name, int amount) {
        if (name2OreMap.containsKey(name)) return get(name, ItemStack.EMPTY, amount);
        ItemStack stack = null;
        List<ItemStack> ores = OreDictionary.getOres(name);
        for (ItemStack ore : ores) {
            if (ore != null && getCapsuleCellContainerCount(ore) == 1) {
                stack = ore.copy().splitStack(amount);
                break;
            }
        }
        return stack;
    }

    public static ItemStack getFirstUnCapsulatedOre(String name, int amount) {
        if (name2OreMap.containsKey(name)) return get(name, ItemStack.EMPTY, amount);
        ItemStack stack = null;
        List<ItemStack> ores = OreDictionary.getOres(name);
        for (ItemStack ore : ores) {
            if (ore != null && getCapsuleCellContainerCount(ore) <= 0) {
                stack = ore.copy().splitStack(amount);
                break;
            }
        }
        return stack;
    }

    public static ItemStack get(String name) {
        return get(name, 1);
    }

    public static ItemStack get(String name, int amount) {
        return get(name, ItemStack.EMPTY, amount);
    }

    public static ItemStack get(String name, ItemStack replacement, int amount) {
        ItemStack stack = name2OreMap.get(name);
        if (stack == null) {
            List<ItemStack> ores = OreDictionary.getOres(name);
            if (!ores.isEmpty()) return ores.get(0);
            else return replacement;
        } else return StackUtil.copyWithSize(stack, amount);
    }

    public static ItemStack get(ItemStack stack) {
        if (stack.isEmpty() || sBlackList.contains(stack)) return stack;
        String name = item2OreMap.get(stack);
        ItemStack ore = null;
        if (name != null) ore = name2OreMap.get(name);

        if (ore == null) ore = stack.copy();
        else ore = ore.copy();

        ore.setCount(stack.getCount());
        return ore;
    }

    public static void addAssociation(String name, ItemStack stack) {
        if (name == null || name.isEmpty() || stack.isEmpty()) return;
        item2OreMap.put(stack, name);
    }

    public static String getAssociation(ItemStack stack) {
        List<String> names = getAssociations(stack);

        return !names.isEmpty() ? names.get(0) : "";
    }

    public static List<String> getAssociations(ItemStack stack) {
        String name = item2OreMap.get(stack);
        if (name == null) {
            int[] ids = OreDictionary.getOreIDs(stack);
            return Arrays.stream(ids)
                    .mapToObj(OreDictionary::getOreName)
                    .collect(Collectors.toList());
        } else return Collections.singletonList(name);
    }

    public static boolean isItemInstanceOf(Block block, String name, boolean prefix) {
        return isItemInstanceOf(new ItemStack(block), name, prefix);
    }

    public static boolean isItemInstanceOf(ItemStack stack, String name, boolean prefix) {
        if (stack.isEmpty() || name == null || name.isEmpty()) return false;

        List<String> names = getAssociations(stack);
        return names.stream()
                .anyMatch(str -> prefix ? str.startsWith(name) : str.equals(name));
    }

    public static boolean registerOre(String name, ItemStack stack) {
        if (name == null || name.isEmpty() || stack.isEmpty()) return false;
        List<ItemStack> ores = OreDictionary.getOres(name);
        for (int i = 0; i < ores.size(); ) {
            if (ores.get(i).isItemEqual(stack))
                return false;
            i++;
        }
        stack = stack.copy().splitStack(1);
        OreDictionary.registerOre(name, stack);
        return true;
    }

    public static String getStackConfigName(ItemStack stack) {
        if (stack.isEmpty()) return null;

        String name = OreDictUnificator.getAssociation(stack);
        if (!name.isEmpty()) return name;
        else if (!(name = stack.getDisplayName()).isEmpty()) return name;

        return stack.getItem().getRegistryName().toString() + ":" + stack.getItemDamage();
    }

    public static int getCapsuleCellContainerCount(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        else if (stack.isItemEqual(emptyCell())) return 1;
        Item item = stack.getItem();
        ItemStack containerItem = item.getContainerItem(stack);
        if (!containerItem.isEmpty() && containerItem.isItemEqual(emptyCell())) return containerItem.getCount();
        String regName = item.getRegistryName().toString();
        if (regName.startsWith(Reference.MODID+":cell_") || regName.equals("forestry:can") ||
                regName.equals("forestry:capsule") || regName.equals("forestry:refractory")) return 1;

        if (stack.isItemEqual(IC2Items.getItem("heat_storage"))) return 1;
        else if (stack.isItemEqual(IC2Items.getItem("tri_heat_storage"))) return 3;
        else if (stack.isItemEqual(IC2Items.getItem("hex_heat_storage"))) return 6;
        else if (stack.isItemEqual(IC2Items.getItem("uranium_fuel_rod"))) return 1;
        else if (stack.isItemEqual(IC2Items.getItem("dual_uranium_fuel_rod"))) return 2;
        else if (stack.isItemEqual(IC2Items.getItem("quad_uranium_fuel_rod"))) return 4;

        return 0;
    }

    private static ItemStack emptyCell() {
        if (emptyCell == null) emptyCell = GregTechMod.classic ? IC2Items.getItem("cell", "empty") : IC2Items.getItem("fluid_cell");
        return emptyCell;
    }
}
