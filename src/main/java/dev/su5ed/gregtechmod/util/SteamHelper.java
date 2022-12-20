package dev.su5ed.gregtechmod.util;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

public final class SteamHelper {

    public static int getSteamForEU(double amount, FluidStack fluidStack) {
        if (fluidStack != null) {
            Fluid fluid = fluidStack.getFluid();
            double multiplier = FluidType.BUCKET_VOLUME / getEUForSteamBucket(fluid);
            return (int) (amount * multiplier);
        }
        return 0;
    }

    public static double getEUForSteam(FluidStack fluid) {
        return fluid != null ? getEUForSteam(fluid, fluid.getAmount()) : 0;
    }

    public static double getEUForSteam(FluidStack fluid, double milibuckets) {
        double buckets = milibuckets / (double) FluidType.BUCKET_VOLUME;
        return getEUForSteamBucket(fluid.getFluid()) * buckets;
    }

    public static double getEUForSteamBucket(Fluid fluid) {
//        return GtFuels.steam.getFuel(fluid).getEnergy();
        return 1; // TODO
    }

    private SteamHelper() {}
}
