package dev.su5ed.gregtechmod.util;

import net.minecraft.world.level.block.Block;

public interface BlockItemProvider extends ItemProvider {
    Block getBlock();
}