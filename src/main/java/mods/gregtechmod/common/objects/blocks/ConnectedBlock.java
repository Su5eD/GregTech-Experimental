package mods.gregtechmod.common.objects.blocks;

import mods.gregtechmod.common.core.ConfigLoader;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ConnectedBlock extends BlockBase {
    public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("connected_north");
    public static final PropertyBool CONNECTED_EAST = PropertyBool.create("connected_east");
    public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("connected_south");
    public static final PropertyBool CONNECTED_WEST = PropertyBool.create("connected_west");
    public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("connected_down");
    public static final PropertyBool CONNECTED_UP = PropertyBool.create("connected_up");

    public ConnectedBlock(String name, Material material, float hardness, float resistance) {
        super(name, material, hardness, resistance);
        setDefaultState(blockState.getBaseState()
                .withProperty(CONNECTED_NORTH, Boolean.FALSE)
                .withProperty(CONNECTED_SOUTH, Boolean.FALSE)
                .withProperty(CONNECTED_EAST, Boolean.FALSE)
                .withProperty(CONNECTED_WEST, Boolean.FALSE));
    }

    @Override
    public int getMetaFromState (IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState (IBlockState state, IBlockAccess world, BlockPos position) {
        return state.withProperty(CONNECTED_DOWN, this.isSideConnectable(world, position, EnumFacing.DOWN))
                .withProperty(CONNECTED_EAST, this.isSideConnectable(world, position, EnumFacing.EAST))
                .withProperty(CONNECTED_NORTH, this.isSideConnectable(world, position, EnumFacing.NORTH))
                .withProperty(CONNECTED_SOUTH, this.isSideConnectable(world, position, EnumFacing.SOUTH))
                .withProperty(CONNECTED_UP, this.isSideConnectable(world, position, EnumFacing.UP))
                .withProperty(CONNECTED_WEST, this.isSideConnectable(world, position, EnumFacing.WEST));
    }

    @Override
    protected BlockStateContainer createBlockState () {

        return new BlockStateContainer(this, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
    }

    private boolean isSideConnectable (IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (!ConfigLoader.connectedMachineCasingTextures) return false;
        final IBlockState state = world.getBlockState(pos.offset(side));
        return state.getBlock() == this;
    }
}
