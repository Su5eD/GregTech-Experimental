package dev.su5ed.gregtechmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;

public class ConnectedBlock extends ResourceBlock {
    // TODO Custom property?
    public static final Property<Boolean> CONNECTED_DOWN = BooleanProperty.create("connected_down");
    public static final Property<Boolean> CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final Property<Boolean> CONNECTED_NORTH = BooleanProperty.create("connected_north");
    public static final Property<Boolean> CONNECTED_SOUTH = BooleanProperty.create("connected_south");
    public static final Property<Boolean> CONNECTED_WEST = BooleanProperty.create("connected_west");
    public static final Property<Boolean> CONNECTED_EAST = BooleanProperty.create("connected_east");

    public ConnectedBlock(float strength, float resistance) {
        super(strength, resistance);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder
            .add(CONNECTED_DOWN)
            .add(CONNECTED_UP)
            .add(CONNECTED_NORTH)
            .add(CONNECTED_SOUTH)
            .add(CONNECTED_WEST)
            .add(CONNECTED_EAST);
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
        return defaultBlockState()
            .setValue(CONNECTED_DOWN, isSideConnectable(world, pos, Direction.DOWN))
            .setValue(CONNECTED_UP, isSideConnectable(world, pos, Direction.UP))
            .setValue(CONNECTED_NORTH, isSideConnectable(world, pos, Direction.NORTH))
            .setValue(CONNECTED_SOUTH, isSideConnectable(world, pos, Direction.SOUTH))
            .setValue(CONNECTED_WEST, isSideConnectable(world, pos, Direction.WEST))
            .setValue(CONNECTED_EAST, isSideConnectable(world, pos, Direction.EAST));
    }

    protected boolean isSideConnectable(Level world, BlockPos pos, Direction side) {
//        if (GregTechConfig.GENERAL.connectedTextures) { TODO Config
        BlockState state = world.getBlockState(pos.relative(side));
        return state.getBlock() == this;
//        }
//        return false;
    }
}
