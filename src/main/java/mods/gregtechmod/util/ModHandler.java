package mods.gregtechmod.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModHandler {

    public static ItemStack getTEBlock(String baseBlock, int meta) {
        Block base = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("thermalexpansion", baseBlock));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getTFItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getRCBlock(String baseItem, int meta) {
        Block base = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("railcraft", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getRCItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("railcraft", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getPRItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("projectred-core", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getFRItem(String baseItem) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base);
    }

    public static ItemStack getTCItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }
}