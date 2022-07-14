package mods.gregtechmod.inventory.tank;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFusionFluid;
import net.minecraft.util.EnumFacing;

import java.util.Collection;

public class GtFluidTankProcessable extends GtFluidTank {

    public <RM extends IGtRecipeManagerFluid<?, ?, ?>> GtFluidTankProcessable(ICoverable parent, String identifier, RM recipeManager, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, int capacity) {
        super(parent, identifier, inputSides, outputSides, recipeManager != null ? recipeManager::hasRecipeFor : f -> false, capacity);
    }
    
    public <RM extends IGtRecipeManagerFusionFluid> GtFluidTankProcessable(ICoverable parent, String identifier, RM recipeManager, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, int capacity) {
        super(parent, identifier, inputSides, outputSides, recipeManager != null ? recipeManager::hasRecipeFor : f -> false, capacity);
    }
}
