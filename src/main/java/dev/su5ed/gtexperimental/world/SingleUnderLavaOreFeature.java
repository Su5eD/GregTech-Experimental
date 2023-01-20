package dev.su5ed.gtexperimental.world;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.function.Function;

public class SingleUnderLavaOreFeature extends SingleOreFeature {

    @Override
    protected boolean canPlaceOreAt(BlockState state, Function<BlockPos, BlockState> adjacentStateAccessor, RandomSource random, OreConfiguration config, OreConfiguration.TargetBlockState targetState, BlockPos.MutableBlockPos mutablePos) {
        BlockState above = adjacentStateAccessor.apply(mutablePos.above());
        return above.is(Blocks.LAVA) && super.canPlaceOreAt(state, adjacentStateAccessor, random, config, targetState, mutablePos);
    }
}
