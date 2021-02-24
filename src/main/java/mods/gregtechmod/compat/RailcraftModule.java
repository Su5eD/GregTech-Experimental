package mods.gregtechmod.compat;

import ic2.core.IC2;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.init.FluidLoader;
import mods.gregtechmod.util.ModHandler;
import mods.railcraft.api.fuel.FluidFuelManager;
import net.minecraftforge.fml.common.Optional;

public class RailcraftModule {

    public static void registerBoilerFuels() {
        if (ModHandler.railcraft) _registerBoilerFuels();
    }

    @Optional.Method(modid = "railcraft")
    private static void _registerBoilerFuels() {
        GregTechAPI.logger.info("Adding fuels to Railcraft's boiler");
        FluidFuelManager.addFuel(FluidLoader.Gas.HYDROGEN.getFluid(), 2000);
        FluidFuelManager.addFuel(FluidLoader.Gas.METHANE.getFluid(), 3000);
        if (IC2.version.isClassic()) FluidFuelManager.addFuel(FluidLoader.Liquid.NITRO_COALFUEL.getFluid(), 18000);
        FluidFuelManager.addFuel(FluidLoader.Liquid.LITHIUM.getFluid(), 24000);
    }
}
