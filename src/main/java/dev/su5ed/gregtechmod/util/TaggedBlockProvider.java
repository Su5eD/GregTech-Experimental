package dev.su5ed.gregtechmod.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface TaggedBlockProvider extends BlockItemProvider {
    TagKey<Block> getBlockTag();
}
