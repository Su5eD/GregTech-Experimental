package mods.gregtechmod.api.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SonictronSound {
    public final ItemStack item;
    public final String name;
    public final int count;

    public SonictronSound(String name, Block block) {
        this(name, new ItemStack(block), 1);
    }

    public SonictronSound(String name, Block block, int count) {
        this(name, new ItemStack(block), count);
    }

    public SonictronSound(String name, Item item) {
        this(name, new ItemStack(item), 1);
    }

    public SonictronSound(String name, Item item, int count) {
        this(name, new ItemStack(item), count);
    }

    public SonictronSound(String name, ItemStack item, int count) {
        this.item = item;
        this.name = name;
        this.count = count;
    }
}
