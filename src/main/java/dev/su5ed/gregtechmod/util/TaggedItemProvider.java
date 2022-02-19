package dev.su5ed.gregtechmod.util;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public interface TaggedItemProvider extends ItemProvider {
    
    Tag.Named<Item> getTag();
}
