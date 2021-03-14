package mods.gregtechmod.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GregTechObjectAPI {
    private static final Logger LOGGER = LogManager.getLogger();
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
            LOGGER.error("The ITEMS map has already been set");
            return;
        }

        ITEMS = map;
    }

    public static void setBlockMap(Map<String, Block> map) {
        if (BLOCKS != null) {
            LOGGER.error("The BLOCKS map has already been set");
            return;
        }

        BLOCKS = map;
    }

    public static void setTileEntityMap(Map<String, ItemStack> map) {
        if (TEBlocks != null) {
            LOGGER.error("The TEBlocks map has already been set");
            return;
        }

        TEBlocks = map;
    }
}