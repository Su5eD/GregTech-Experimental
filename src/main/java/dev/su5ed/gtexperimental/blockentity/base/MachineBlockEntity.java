package dev.su5ed.gtexperimental.blockentity.base;

import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends UpgradableBlockEntityImpl {

    public MachineBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }
}
