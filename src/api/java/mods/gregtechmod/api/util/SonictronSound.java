package mods.gregtechmod.api.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class SonictronSound {
    private final ItemStack item;
    private final String name;
    private final int count;

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

    public ItemStack getItem() {
        return this.item;
    }

    public String getName() {
        return this.name;
    }

    public int getCount() {
        return this.count;
    }
}
