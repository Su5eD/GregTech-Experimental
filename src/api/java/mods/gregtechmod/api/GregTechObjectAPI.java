package mods.gregtechmod.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@SuppressWarnings({ "unused", "MismatchedQueryAndUpdateOfCollection" })
public class GregTechObjectAPI {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Map<String, ItemStack> items;
    private static Map<String, Block> blocks;
    private static Map<String, ItemStack> teBlocks;

    public static ItemStack getItem(String name) {
        ItemStack stack = items.get(name);
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public static Block getBlock(String name) {
        return blocks.get(name);
    }

    public static ItemStack getTileEntity(String name) {
        return teBlocks.get(name);
    }
}
