package dev.su5ed.gregtechmod.blockentity;

import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseBlockEntity extends BlockEntity {
    
    protected BaseBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        this(provider.getType(), pos, state);
    }

    protected BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public void tickServer() {}
}
