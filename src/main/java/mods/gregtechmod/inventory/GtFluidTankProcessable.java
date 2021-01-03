package mods.gregtechmod.inventory;

import com.google.common.base.Predicates;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFluid;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;

public class GtFluidTankProcessable<RM extends IGtRecipeManagerFluid<?, ?, ?>> extends GtFluidTank {
    private final RM recipeManager;

    public GtFluidTankProcessable(ICoverable parent, String identifier, RM recipeManager, int capacity) {
        this(parent, identifier, recipeManager, Util.allFacings, Util.allFacings, capacity);
    }

    public GtFluidTankProcessable(ICoverable parent, String identifier, RM recipeManager, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, int capacity) {
        super(parent, identifier, inputSides, outputSides, Predicates.alwaysFalse(), capacity);
        this.recipeManager = recipeManager;
    }

    @Override
    public boolean acceptsFluid(Fluid fluid) {
        return this.recipeManager.hasRecipeFor(fluid);
    }
}
