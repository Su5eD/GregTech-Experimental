package dev.su5ed.gtexperimental.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public interface FluidProvider {
    String getFluidRegistryName();

    FluidType getType();

    FlowingFluid getSourceFluid();

    FlowingFluid getFlowingFluid();

    @Nullable
    TagKey<Fluid> getTag();

    default FluidStack getBuckets(int count) {
        return getFluidStack(buckets(count));
    }

    default FluidStack getFluidStack(int amount) {
        return new FluidStack(getSourceFluid(), amount);
    }
}
