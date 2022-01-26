package mods.gregtechmod.util;

import ic2.core.util.StackUtil;
import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.stream.Collectors;

public class OreDictUnificator {
    private static final Map<String, ItemStack> NAME_TO_ITEM = new HashMap<>();
    private static final Map<ItemStack, String> ITEM_TO_ORE = new HashMap<>();
    private static final List<ItemStack> BLACK_LIST = new ArrayList<>();
    
    public static void addToBlacklist(ItemStack stack) {
        BLACK_LIST.add(stack);
    }
    
    public static boolean isBlacklisted(ItemStack stack) {
        return BLACK_LIST.stream()
                .anyMatch(ore -> GtUtil.stackEquals(ore, stack));
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
        ItemStack ore = StackUtil.copyWithSize(stack, 1);
        addAssociation(name, ore);
        if (!NAME_TO_ITEM.containsKey(name)) NAME_TO_ITEM.put(name, ore);
        else if (overwrite && Arrays.asList(GregTechConfig.UNIFICATION.specialUnificationTargets).contains(getStackConfigName(ore))) {
            NAME_TO_ITEM.put(name, ore);
        }
        registerOre(name, ore);
    }

    public static void override(String name, ItemStack stack) {
        if (!name.startsWith("itemDust")
                && !stack.isEmpty()
                && (stack.getDisplayName().isEmpty() || Arrays.asList(GregTechConfig.UNIFICATION.specialUnificationTargets).contains(getStackConfigName(stack)))) {
            set(name, stack);
        }
    }

    public static ItemStack getUnifiedOre(String name) {
        return getUnifiedOre(name, ItemStack.EMPTY);
    }
    
    public static ItemStack getUnifiedOre(String name, ItemStack defaultValue) {
        ItemStack stack = NAME_TO_ITEM.get(name);
        return stack == null ? defaultValue : stack;
    }
    
    public static boolean oreExists(String ore) {
        return !OreDictionary.getOres(ore).isEmpty();
    }

    public static OptionalItemStack getFirstOre(String name) {
        return getFirstOre(name, 1);
    }

    public static OptionalItemStack getFirstOre(String name, int count) {
        if (NAME_TO_ITEM.containsKey(name)) return OptionalItemStack.of(get(name, ItemStack.EMPTY, count));
        
        List<ItemStack> ores = OreDictionary.getOres(name);
        if (!ores.isEmpty()) {
            ItemStack stack = StackUtil.copyWithSize(ores.get(0), count);
            return OptionalItemStack.of(stack);
        }
        else return OptionalItemStack.EMPTY;
    }

    public static ItemStack get(String name) {
        return get(name, 1);
    }

    public static ItemStack get(String name, int amount) {
        return get(name, ItemStack.EMPTY, amount);
    }

    public static ItemStack get(String name, ItemStack replacement, int amount) {
        ItemStack stack = NAME_TO_ITEM.get(name);
        if (stack == null) {
            List<ItemStack> ores = OreDictionary.getOres(name);
            return !ores.isEmpty() ? ores.get(0) : replacement;
        }
        return StackUtil.copyWithSize(stack, amount);
    }
    
    public static ItemStack get(ItemStack oreStack) {
        if (!oreStack.isEmpty() && !isBlacklisted(oreStack)) {
            String name = getAssociation(oreStack);
            if (name != null) {
                ItemStack item = NAME_TO_ITEM.get(name);
                return item != null ? ItemHandlerHelper.copyStackWithSize(item, oreStack.getCount()) : oreStack.copy();
            }
        }
        return oreStack;
      }

    public static void addAssociation(String name, ItemStack stack) {
        ITEM_TO_ORE.put(stack, name);
    }

    public static String getAssociation(ItemStack stack) {
        List<String> names = getAssociations(stack);
        return !names.isEmpty() ? names.get(0) : "";
    }

    public static List<String> getAssociations(ItemStack stack) {
        String name = ITEM_TO_ORE.get(stack);
        if (name == null) {
            return Arrays.stream(OreDictionary.getOreIDs(stack))
                    .mapToObj(OreDictionary::getOreName)
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(name);
    }

    public static boolean isItemInstanceOf(Block block, String name, boolean prefix) {
        return isItemInstanceOf(new ItemStack(block), name, prefix);
    }

    public static boolean isItemInstanceOf(ItemStack stack, String name, boolean prefix) {
        return getAssociations(stack).stream()
                .anyMatch(str -> prefix ? str.startsWith(name) : str.equals(name));
    }

    public static void registerOre(String name, ItemStack stack) {
        if (!stack.isEmpty()) {
            boolean nonexistent = OreDictionary.getOres(name).stream()
                    .noneMatch(stack::isItemEqual);
            if (nonexistent) {
                ItemStack ore = StackUtil.copyWithSize(stack, 1);
                OreDictionary.registerOre(name, ore);
            }
        }
    }

    public static String getStackConfigName(ItemStack stack) {
        String name = OreDictUnificator.getAssociation(stack);
        if (!name.isEmpty()) return name;
        else {
            String displayName = stack.getDisplayName();
            if (!displayName.isEmpty()) return displayName;
        }

        return stack.getItem().getRegistryName().toString() + ":" + stack.getItemDamage();
    }
}
