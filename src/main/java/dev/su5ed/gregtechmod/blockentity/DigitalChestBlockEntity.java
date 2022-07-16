package dev.su5ed.gregtechmod.blockentity;

import dev.su5ed.gregtechmod.blockentity.base.CoverableBlockEntity;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DigitalChestBlockEntity extends CoverableBlockEntity {
    
    public DigitalChestBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }

    public void upgradeToQuantumChest() {
        throw new UnsupportedOperationException();
    }
}
