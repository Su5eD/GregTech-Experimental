package mods.gregtechmod.api.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class SonictronSound {
    public final Item item;
    public final String name;
    public final int count;

    public SonictronSound(Block block, String name, int count) {
        this(Item.getItemFromBlock(block), name, count);
    }

    public SonictronSound(Item item, String name, int count) {
        this.item = item;
        this.name = name;
        this.count = count;
    }
}
