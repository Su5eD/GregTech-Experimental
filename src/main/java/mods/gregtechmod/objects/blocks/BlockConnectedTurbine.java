package mods.gregtechmod.objects.blocks;

import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.struct.Rotor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockConnectedTurbine extends BlockConnected {
    public static final IProperty<Boolean> TURBINE = PropertyBool.create("turbine");
    public static final IProperty<Rotor> TURBINE_ROTOR = new Rotor.PropertyRotor();

    public BlockConnectedTurbine(String name) {
        super(name);
    }

    @Override
    protected IBlockState getBaseState() {
        return super.getBaseState()
            .withProperty(BlockConnectedTurbine.TURBINE, false)
            .withProperty(BlockConnectedTurbine.TURBINE_ROTOR, Rotor.DISABLED);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
            .add(TURBINE, TURBINE_ROTOR, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST)
            .build();
    }

    @Override
    protected boolean isSideConnectable(IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (!GregTechConfig.GENERAL.connectedTextures) return false;
        IBlockState state = world.getBlockState(pos.offset(side));
        IBlockState current = world.getBlockState(pos);
        if (isTurbine(current) && !isTurbine(state) || !isTurbine(current) && isTurbine(state)) return false;
        return state.getBlock() == this;
    }

    private boolean isTurbine(IBlockState state) {
        return Boolean.TRUE.equals(GtUtil.getStateValueSafely(state, BlockConnectedTurbine.TURBINE));
    }
}
