package mods.gregtechmod.inventory.tank;

import com.google.common.base.Predicate;
import mods.gregtechmod.api.cover.ICoverable;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import java.util.Collections;

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
        return this.inputPredicate.apply(side);
    }

    @Override
    public boolean canDrain(EnumFacing side) {
        return this.outputPredicate.apply(side);
    }
}
