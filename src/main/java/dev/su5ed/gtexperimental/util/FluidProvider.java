package dev.su5ed.gtexperimental.util;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fluids.FluidType;

public interface FluidProvider {
    String getFluidRegistryName();

    FluidType getType();
    
    FlowingFluid getSourceFluid();
    
    FlowingFluid getFlowingFluid();
}
