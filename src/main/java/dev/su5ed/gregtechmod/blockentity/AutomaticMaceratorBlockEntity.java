package dev.su5ed.gregtechmod.blockentity;

import dev.su5ed.gregtechmod.object.GTBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AutomaticMaceratorBlockEntity extends CoverableBlockEntity {

    public AutomaticMaceratorBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.AUTO_MACERATOR, pos, state);
    }
}
