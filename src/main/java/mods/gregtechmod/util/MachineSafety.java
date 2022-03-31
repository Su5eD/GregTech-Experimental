package mods.gregtechmod.util;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Arrays;

public class MachineSafety {

    public static <T extends TileEntity & IElectricMachine> void checkSafety(T machine) {
        World world = machine.getWorld();
        BlockPos pos = machine.getPos();

        double capacity = machine instanceof IUpgradableMachine ? ((IUpgradableMachine) machine).getUniversalEnergyCapacity() : machine.getEUCapacity();
        if (capacity > 0) {
            if (GregTechConfig.MACHINES.machineFireExplosions && world.rand.nextInt(1000) == 0 && world.getBlockState(pos.offset(EnumFacing.random(world.rand))).getBlock() == Blocks.FIRE) {
                machine.markForExplosion();
            }

            if (machine instanceof ICoverable && ((ICoverable) machine).getCoverAtSide(EnumFacing.UP) == null) {
                if (world.getPrecipitationHeight(pos).getY() - 2 < pos.getY()) {
                    if (GregTechConfig.MACHINES.machineRainExplosions && world.rand.nextInt(1000) == 0 && world.isRaining()) {
                        if (world.rand.nextInt(10) == 0) {
                            machine.markForExplosion();
                        }
                        else if (GregTechConfig.MACHINES.machineFlammable) {
                            setBlockOnFire(world, pos);
                        }
                    }
                    if (GregTechConfig.MACHINES.machineThunderExplosions && world.rand.nextInt(2500) == 0 && world.isThundering()) {
                        machine.markForExplosion();
                    }
                }
            }
        }
    }

    public static void setBlockOnFire(World world, BlockPos pos) {
        Arrays.stream(EnumFacing.VALUES)
            .map(pos::offset)
            .filter(world::isAirBlock)
            .forEach(offset -> world.setBlockState(offset, Blocks.FIRE.getDefaultState(), Constants.BlockFlags.DEFAULT_AND_RERENDER));
    }
}
