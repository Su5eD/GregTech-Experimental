package dev.su5ed.gtexperimental.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.stream.Stream;

public final class MachineSafety {

    public static void explodeMachine(BlockEntity be, float power) {
        BlockPos pos = be.getBlockPos();
        Level level = be.getLevel();
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power, Explosion.BlockInteraction.BREAK);
    }

    public static void setBlockOnFire(Level level, BlockPos pos) {
        Stream.of(Direction.values())
            .map(pos::relative)
            .filter(level::isEmptyBlock)
            .forEach(offset -> level.setBlockAndUpdate(offset, Blocks.FIRE.defaultBlockState()));
    }

    private MachineSafety() {}
}
