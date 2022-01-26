package mods.gregtechmod.inventory.tank;

import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import net.minecraft.util.EnumFacing;

import java.util.Collection;

@SuppressWarnings("Guava")
public class GtFluidTankProcessableFuel<FM extends IFuelManagerFluid<?>> extends GtFluidTank {
    
    public GtFluidTankProcessableFuel(ICoverable parent, String identifier, FM fuelManager, int capacity) {
        this(parent, identifier, fuelManager, Util.allFacings, Util.allFacings, capacity);
    }

    public GtFluidTankProcessableFuel(ICoverable parent, String identifier, FM fuelManager, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, int capacity) {
        super(parent, identifier, inputSides, outputSides, fuelManager != null ? fuelManager::hasFuel : fluid1 -> false, capacity);
    }
}
