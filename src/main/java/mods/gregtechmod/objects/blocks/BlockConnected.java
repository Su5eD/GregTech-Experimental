package mods.gregtechmod.objects.blocks;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.ICustomItemModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Locale;

public class BlockConnected extends BlockBase implements ICustomItemModel {
    public static final IProperty<Boolean> CONNECTED_DOWN = PropertyBool.create("connected_down");
    public static final IProperty<Boolean> CONNECTED_UP = PropertyBool.create("connected_up");
    public static final IProperty<Boolean> CONNECTED_NORTH = PropertyBool.create("connected_north");
    public static final IProperty<Boolean> CONNECTED_SOUTH = PropertyBool.create("connected_south");
    public static final IProperty<Boolean> CONNECTED_WEST = PropertyBool.create("connected_west");
    public static final IProperty<Boolean> CONNECTED_EAST = PropertyBool.create("connected_east");

    private final String name;

    public BlockConnected(String name) {
        super(Material.IRON);
        this.name = name.toLowerCase(Locale.ROOT);
        setDefaultState(getBaseState());
    }

    protected IBlockState getBaseState() {
        return blockState.getBaseState()
            .withProperty(CONNECTED_DOWN, false)
            .withProperty(CONNECTED_UP, false)
            .withProperty(CONNECTED_NORTH, false)
            .withProperty(CONNECTED_SOUTH, false)
            .withProperty(CONNECTED_WEST, false)
            .withProperty(CONNECTED_EAST, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos position) {
        return state
            .withProperty(CONNECTED_DOWN, isSideConnectable(world, position, EnumFacing.DOWN))
            .withProperty(CONNECTED_UP, isSideConnectable(world, position, EnumFacing.UP))
            .withProperty(CONNECTED_NORTH, isSideConnectable(world, position, EnumFacing.NORTH))
            .withProperty(CONNECTED_SOUTH, isSideConnectable(world, position, EnumFacing.SOUTH))
            .withProperty(CONNECTED_WEST, isSideConnectable(world, position, EnumFacing.WEST))
            .withProperty(CONNECTED_EAST, isSideConnectable(world, position, EnumFacing.EAST));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
            .add(CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST)
            .build();
    }

    protected boolean isSideConnectable(IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (GregTechConfig.GENERAL.connectedTextures) {
            IBlockState state = world.getBlockState(pos.offset(side));
            return state.getBlock() == this;
        }
        return false;
    }

    @Override
    public ResourceLocation getItemModel() {
        return new ResourceLocation(Reference.MODID, "block_" + this.name);
    }
}
