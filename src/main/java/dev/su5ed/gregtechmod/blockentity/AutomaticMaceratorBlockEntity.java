package dev.su5ed.gregtechmod.blockentity;

import dev.su5ed.gregtechmod.blockentity.base.ElectricBlockEntity;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AutomaticMaceratorBlockEntity extends ElectricBlockEntity {

    public AutomaticMaceratorBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.AUTO_MACERATOR, pos, state);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 0;
    }
}
