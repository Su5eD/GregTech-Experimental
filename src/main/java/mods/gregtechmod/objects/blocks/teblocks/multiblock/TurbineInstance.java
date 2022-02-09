package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import mods.gregtechmod.objects.blocks.BlockConnectedTurbine;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.struct.Rotor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TurbineInstance extends TileEntityMultiBlockBase.MultiBlockInstance {
    private final World world;
    private final Map<BlockPos, String> rotorTextures;

    public TurbineInstance(EnumFacing facing, World world, Map<Character, Collection<BlockPos>> elements, boolean active) {
        super(world, elements);
        this.world = world;

        Collection<BlockPos> rotorCasings = elements.get('R');
        if (rotorCasings == null) throw new IllegalArgumentException("Could not find the 'R' structure element");
        List<BlockPos> sorted = rotorCasings.stream()
            .sorted(Comparator.comparing(Function.identity(), (one, two) -> compareBlockPos(facing, one, two)))
            .collect(Collectors.toList());
        this.rotorTextures = JavaUtil.zipToMap(sorted, Rotor.TEXTURE_PARTS);

        setTurbineProperty(true);
        setRotorProperty(facing, active);
    }

    private static int compareBlockPos(EnumFacing facing, BlockPos one, BlockPos two) {
        if (two.getY() == one.getY()) {
            if (two.getZ() == one.getZ()) {
                if (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                    return one.getX() - two.getX();
                }
                else return two.getX() - one.getX();
            }
            else {
                if (facing.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) {
                    return one.getZ() - two.getZ();
                }
                else return two.getZ() - one.getZ();
            }
        }
        else return two.getY() - one.getY();
    }

    public void setRotorProperty(EnumFacing facing, boolean active) {
        this.rotorTextures
            .forEach((pos, texture) -> {
                IBlockState state = this.world.getBlockState(pos);

                if (state.getBlock() instanceof BlockConnectedTurbine) {
                    Rotor rotor;
                    if (facing == null) rotor = Rotor.DISABLED;
                    else rotor = Rotor.getRotor(facing, texture, active);
                    
                    IBlockState newState = state.withProperty(BlockConnectedTurbine.TURBINE_ROTOR, rotor);
                    this.world.setBlockState(pos, newState, Constants.BlockFlags.SEND_TO_CLIENTS | Constants.BlockFlags.RERENDER_MAIN_THREAD);
                }
            });
    }

    public void setTurbineProperty(boolean value) {
        this.positions
            .forEach(pos -> {
                IBlockState state = this.world.getBlockState(pos);
                if (state.getBlock() instanceof BlockConnectedTurbine) {
                    IBlockState newState = state.withProperty(BlockConnectedTurbine.TURBINE, value);
                    this.world.setBlockState(pos, newState, Constants.BlockFlags.DEFAULT_AND_RERENDER);
                }
            });
    }
}
