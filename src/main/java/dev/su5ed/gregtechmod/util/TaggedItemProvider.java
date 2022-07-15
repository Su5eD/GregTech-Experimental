package dev.su5ed.gregtechmod.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface TaggedItemProvider extends ItemProvider {
    TagKey<Item> getTag();
}
