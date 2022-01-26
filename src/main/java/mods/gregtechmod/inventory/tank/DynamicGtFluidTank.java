package mods.gregtechmod.inventory.tank;

import mods.gregtechmod.api.cover.ICoverable;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import java.util.Collections;
import java.util.function.Predicate;

public class DynamicGtFluidTank extends GtFluidTank {
    private final Predicate<EnumFacing> inputPredicate;
    private final Predicate<EnumFacing> outputPredicate;

    public DynamicGtFluidTank(ICoverable parent, String identifier, Predicate<EnumFacing> inputPredicate, Predicate<EnumFacing> outputPredicate, Predicate<Fluid> acceptedFluids, int capacity) {
        super(parent, identifier, Collections.emptySet(), Collections.emptySet(), acceptedFluids, capacity);
        
        this.inputPredicate = inputPredicate;
        this.outputPredicate = outputPredicate;
    }

    @Override
    public boolean canFill(EnumFacing side) {
        return this.inputPredicate.test(side);
    }

    @Override
    public boolean canDrain(EnumFacing side) {
        return this.outputPredicate.test(side);
    }
}
