package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.blockentity.base.UpgradableBlockEntityImpl;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

public class AutomaticMaceratorBlockEntity extends UpgradableBlockEntityImpl {

    public AutomaticMaceratorBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.AUTO_MACERATOR, pos, state);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000;
    }

    @Override
    public Collection<Direction> getSinkSides() {
        return GtUtil.ALL_FACINGS;
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }
}
