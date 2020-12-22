package mods.gregtechmod.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class GregTechObjectAPI {
    private static Map<String, ItemStack> ITEMS = null;
    private static Map<String, Block> BLOCKS = null;
    private static Map<String, ItemStack> TEBlocks = null;

    public static ItemStack getItem(String name) {
        ItemStack stack = ITEMS.get(name);
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public static Block getBlock(String name) {
        return BLOCKS.get(name);
    }

    public static ItemStack getTileEntity(String name) {
        return TEBlocks.get(name);
    }

    public static void setItemMap(Map<String, ItemStack> map) {
        if (ITEMS != null) {
            GregTechAPI.logger.error("Tried to set the ITEMS map for the GregTechObjectAPI again!");
            return;
        }

        ITEMS = map;
    }

    public static void setBlockMap(Map<String, Block> map) {
        if (BLOCKS != null) {
            GregTechAPI.logger.error("Tried to set the BLOCKS map for the GregTechObjectAPI again!");
            return;
        }

        BLOCKS = map;
    }

    public static void setTileEntityMap(Map<String, ItemStack> map) {
        if (TEBlocks != null) {
            GregTechAPI.logger.error("Tried to set the TEBlocks map for the GregTechObjectAPI again!");
            return;
        }

        TEBlocks = map;
    }
}