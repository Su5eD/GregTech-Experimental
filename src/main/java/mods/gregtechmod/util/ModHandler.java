package mods.gregtechmod.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModHandler {

    public static ItemStack getTEBlock(String baseBlock, int meta) {
        Block base = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("thermalexpansion", baseBlock));
        if (base == null) throw new IllegalStateException("Failed to fetch ThermalExpansion block '"+baseBlock+"' with meta '"+meta+"'");
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getTEItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalexpansion", baseItem));
        if (base == null) throw new IllegalStateException("Failed to fetch ThermalExpansion item '"+baseItem+"' with meta '"+meta+"'");
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getRCBlock(String baseItem, int meta) {
        Block base = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("railcraft", baseItem));
        if (base == null) throw new IllegalStateException("Failed to fetch Railcraft item '"+baseItem+"' with meta '"+meta+"'");
        return new ItemStack(base, 1, meta);
    }
}