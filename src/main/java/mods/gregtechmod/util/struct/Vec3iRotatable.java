package mods.gregtechmod.util.struct;

import ic2.core.util.Util;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class Vec3iRotatable extends Vec3i {

    public Vec3iRotatable(int x, int y, int z) {
        super(x, y, z);
    }
    
    public Vec3iRotatable relativise(Vec3i other) {
        return new Vec3iRotatable(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
    }
    
    public Vec3i rotateHorizontal(EnumFacing facing) {
        if (Util.horizontalFacings.contains(facing)) {
            if (facing.getAxis() == EnumFacing.Axis.Z && facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                return new Vec3i(-getX(), getY(), -getZ());
            } else if (facing.getAxis() == EnumFacing.Axis.X) {
                if (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                    return new Vec3i(-getZ(), getY(), -getX());
                } else {
                    return new Vec3i(getZ(), getY(), getX());
                }
            }
        }
        return this;
    }
}
