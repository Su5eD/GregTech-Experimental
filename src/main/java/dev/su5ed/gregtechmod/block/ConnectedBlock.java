package dev.su5ed.gregtechmod.block;

import dev.su5ed.gregtechmod.GregTechConfig;
import dev.su5ed.gregtechmod.model.DirectionsProperty;
import dev.su5ed.gregtechmod.model.DirectionsProperty.DirectionsWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;

public class ConnectedBlock extends ResourceBlock {
    public static final Property<DirectionsWrapper> DIRECTIONS = new DirectionsProperty("directions");

    public ConnectedBlock(float strength, float resistance) {
        super(strength, resistance);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DIRECTIONS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        if (pState.getBlock() == this) pLevel.setBlockAndUpdate(pPos, getActualState(pLevel, pPos));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level world = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        
        return getActualState(world, pos);
    }

    public BlockState getActualState(Level world, BlockPos pos) {
        Direction[] dirs = StreamEx.of(Direction.values())
            .filter(side -> isSideConnectable(world, pos, side))
            .toArray(Direction[]::new);
        return defaultBlockState()
            .setValue(DIRECTIONS, DirectionsWrapper.from(dirs));
    }

    protected boolean isSideConnectable(Level world, BlockPos pos, Direction side) {
        if (GregTechConfig.CLIENT.connectedTextures.get()) {
            BlockState state = world.getBlockState(pos.relative(side));
            return state.getBlock() == this;
        }
        return false;
    }
}
