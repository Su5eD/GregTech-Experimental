package mods.gregtechmod.recipe.util;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class SteamHelper {

    public static int getSteamForEU(double amount, @Nullable FluidStack fluidStack) {
        if (fluidStack != null) {
            Fluid fluid = fluidStack.getFluid();
            double multiplier = Fluid.BUCKET_VOLUME / getEUForSteamBucket(fluid);
            return (int) (amount * multiplier);
        }
        return 0;
    }

    public static double getEUForSteam(@Nullable FluidStack fluid) {
        return fluid != null ? getEUForSteam(fluid, fluid.amount) : 0;
    }

    public static double getEUForSteam(@Nullable FluidStack fluid, double milibuckets) {
        double buckets = milibuckets / (double) Fluid.BUCKET_VOLUME;
        return fluid != null ? getEUForSteamBucket(fluid.getFluid()) * buckets : 0;
    }

    public static double getEUForSteamBucket(Fluid fluid) {
        return GtFuels.steam.getFuel(fluid).getEnergy();
    }
}
