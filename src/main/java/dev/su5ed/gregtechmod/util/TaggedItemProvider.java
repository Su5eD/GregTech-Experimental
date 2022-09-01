package dev.su5ed.gregtechmod.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public interface TaggedItemProvider extends ItemProvider {
    @Nullable
    TagKey<Item> getTag();
}
