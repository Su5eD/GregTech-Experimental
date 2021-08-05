package mods.gregtechmod.inventory.tank;

import com.google.common.base.Predicates;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFluid;
import net.minecraft.util.EnumFacing;

import java.util.Collection;

@SuppressWarnings("Guava")
public class GtFluidTankProcessable<RM extends IGtRecipeManagerFluid<?, ?, ?>> extends GtFluidTank {

    public GtFluidTankProcessable(ICoverable parent, String identifier, RM recipeManager, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, int capacity) {
        super(parent, identifier, inputSides, outputSides, recipeManager != null ? recipeManager::hasRecipeFor : Predicates.alwaysFalse(), capacity);
    }
}
