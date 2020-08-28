package mods.gregtechmod.common.inventory;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import ic2.core.block.comp.Fluids;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;

public class GtFluidTank extends Fluids.InternalFluidTank {
    private final ICoverable parent;

    public GtFluidTank(ICoverable parent, String identifier, int capacity) {
        super(identifier, Util.allFacings, Util.allFacings, Predicates.alwaysTrue(), capacity);
        this.parent = parent;
    }

    public GtFluidTank(ICoverable parent, String identifier, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, Predicate<Fluid> acceptedFluids, int capacity) {
        super(identifier, inputSides, outputSides, acceptedFluids, capacity);
        this.parent = parent;
    }

    @Override
    public boolean canFill(EnumFacing side) {
        ICover cover = parent.getCoverAtSide(side);
        if (cover != null) {
            return true;
        }
        return super.canFill(side);
    }

    @Override
    public boolean canDrain(EnumFacing side) {
        ICover cover = parent.getCoverAtSide(side);
        if (cover != null) return cover.letsLiquidsOut();
        return super.canDrain(side);
    }
}
