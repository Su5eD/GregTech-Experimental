package dev.su5ed.gregtechmod.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public enum HarvestLevel {
    STONE(BlockTags.NEEDS_STONE_TOOL),
    IRON(BlockTags.NEEDS_IRON_TOOL),
    DIAMOND(BlockTags.NEEDS_DIAMOND_TOOL);
    
    private final TagKey<Block> tag;

    HarvestLevel(TagKey<Block> tag) {
        this.tag = tag;
    }

    public TagKey<Block> getTag() {
        return this.tag;
    }
}
