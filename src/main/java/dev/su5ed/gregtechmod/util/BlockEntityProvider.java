package dev.su5ed.gregtechmod.util;

import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockEntityProvider {
    BlockEntityType<?> getType();
}
