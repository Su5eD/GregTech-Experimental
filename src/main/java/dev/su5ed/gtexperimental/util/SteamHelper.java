package dev.su5ed.gtexperimental.util;

import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

public final class SteamHelper {

    public static int getSteamForEU(Level level, double amount, FluidStack fluidStack) {
        if (fluidStack != null) {
            Fluid fluid = fluidStack.getFluid();
            double multiplier = FluidType.BUCKET_VOLUME / getEUForSteamBucket(level, fluid);
            return (int) (amount * multiplier);
        }
        return 0;
    }

    public static double getEUForSteam(Level level, FluidStack fluid) {
        return fluid != null ? getEUForSteam(level, fluid, fluid.getAmount()) : 0;
    }

    public static double getEUForSteam(Level level, FluidStack fluid, double milibuckets) {
        double buckets = milibuckets / (double) FluidType.BUCKET_VOLUME;
        return getEUForSteamBucket(level, fluid.getFluid()) * buckets;
    }

    public static double getEUForSteamBucket(Level level, Fluid fluid) {
        SISORecipe<FluidStack, FluidStack> recipe = ModRecipeManagers.STEAM_FUEL.getRecipeFor(level, new FluidStack(fluid, FluidType.BUCKET_VOLUME));
        return recipe != null ? recipe.getProperty(ModRecipeProperty.ENERGY) : 0;
    }

    private SteamHelper() {}
}
