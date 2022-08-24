package mods.gregtechmod.util;

import net.minecraft.block.Block;

public interface IBlockItemProvider extends IItemProvider {
    Block getBlockInstance();
}
