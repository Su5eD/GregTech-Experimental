package dev.su5ed.gregtechmod.api.util;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface GtFluidTank extends IFluidTank {
    String getName();
    
    CompoundTag save();

    void load(CompoundTag tag);

    void setFluid(FluidStack fluid);

    boolean canFill(Direction side);

    boolean canDrain(Direction side);
    
    IFluidTank setCapacity(int capacity);
}
