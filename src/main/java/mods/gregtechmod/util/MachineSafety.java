package mods.gregtechmod.util;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MachineSafety {

    public static <T extends TileEntity & IGregtechMachine> void checkSafety(T machine) {
        if (machine.getUniversalEnergyCapacity() > 0) {
            if (GregTechConfig.MACHINES.machineFireExplosions && GtUtil.RANDOM.nextInt(1000) == 0 && machine.getWorld().getBlockState(machine.getPos().offset(EnumFacing.random(GtUtil.RANDOM))).getBlock() == Blocks.FIRE) {
                machine.markForExplosion();
            }

            if (machine instanceof ICoverable && ((ICoverable)machine).getCoverAtSide(EnumFacing.UP) == null) {
                BlockPos pos = machine.getPos();
                if (machine.getWorld().getPrecipitationHeight(pos).getY() - 2 < pos.getY()) {
                    if (GregTechConfig.MACHINES.machineRainExplosions && GtUtil.RANDOM.nextInt(1000) == 0 && machine.getWorld().isRaining()) {
                        if (GtUtil.RANDOM.nextInt(10)==0) {
                            machine.markForExplosion();
                        } else if (GregTechConfig.MACHINES.machineFlammable) {
                            setBlockOnFire(machine.getWorld(), machine.getPos());
                        }
                    }
                    if (GregTechConfig.MACHINES.machineThunderExplosions && GtUtil.RANDOM.nextInt(2500) == 0 && machine.getWorld().isThundering()) {
                        machine.markForExplosion();
                    }
                }
            }
        }
    }

    public static void setBlockOnFire(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = pos.offset(facing);
            if (world.isAirBlock(offset))
                world.setBlockState(offset, Blocks.FIRE.getDefaultState(), 11);
        }
    }
}
