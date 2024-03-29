package mods.gregtechmod.inventory.tank;

import ic2.core.block.comp.Fluids;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;
import java.util.function.Predicate;

public class GtFluidTank extends Fluids.InternalFluidTank {
    private final ICoverable parent;

    public GtFluidTank(ICoverable parent, String identifier, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, Predicate<Fluid> acceptedFluids, int capacity) {
        super(identifier, inputSides, outputSides, acceptedFluids::test, capacity);
        this.parent = parent;
    }

    @Override
    public boolean canFill(EnumFacing side) {
        if (this.parent instanceof IGregTechMachine && !((IGregTechMachine) this.parent).isAllowedToWork()) return false;

        ICover cover = parent.getCoverAtSide(side);
        return super.canFill(side) && (cover == null || cover.letsLiquidsIn());
    }

    @Override
    public boolean canDrain(EnumFacing side) {
        if (this.parent instanceof IGregTechMachine && !((IGregTechMachine) this.parent).isAllowedToWork()) return false;

        ICover cover = parent.getCoverAtSide(side);
        return super.canDrain(side) && (cover == null || cover.letsLiquidsOut());
    }

    public boolean isEmpty() {
        return getFluidAmount() <= 0;
    }
}
