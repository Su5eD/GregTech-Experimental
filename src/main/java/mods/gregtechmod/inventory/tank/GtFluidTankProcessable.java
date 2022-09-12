package mods.gregtechmod.inventory.tank;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import net.minecraft.util.EnumFacing;

import java.util.Collection;

public class GtFluidTankProcessable extends GtFluidTank {

    public GtFluidTankProcessable(ICoverable parent, String identifier, IGtRecipeManagerCellular recipeManager, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, int capacity) {
        super(parent, identifier, inputSides, outputSides, recipeManager != null ? recipeManager::hasRecipeFor : f -> false, capacity);
    }
}
