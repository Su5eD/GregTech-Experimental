package dev.su5ed.gtexperimental.util;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.cover.Cover;
import dev.su5ed.gtexperimental.api.cover.CoverHandler;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.api.util.GtFluidTank;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class GtFluidTankImpl extends FluidTank implements GtFluidTank {
    private final String name;
    private final CoverHandler coverHandler;
    private final MachineController controller;
    private final Collection<Direction> inputSides;
    private final Collection<Direction> outputSides;

    public GtFluidTankImpl(String name, ICapabilityProvider provider, int capacity, Predicate<FluidStack> validator, Collection<Direction> inputSides, Collection<Direction> outputSides) {
        super(capacity, validator);

        this.name = name;
        this.coverHandler = provider.getCapability(Capabilities.COVER_HANDLER).orElse(null);
        this.controller = provider.getCapability(Capabilities.MACHINE_CONTROLLER).orElse(null);
        this.inputSides = inputSides;
        this.outputSides = outputSides;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CompoundTag save() {
        return writeToNBT(new CompoundTag());
    }

    @Override
    public void load(CompoundTag tag) {
        readFromNBT(tag);
    }

    @Override
    public boolean canFill(Direction side) {
        if (this.controller == null || this.controller.isAllowedToWork()) {
            boolean allowFluids = Optional.ofNullable(this.coverHandler)
                .flatMap(handler -> handler.getCoverAtSide(side))
                .map(Cover::letsLiquidsIn)
                .orElse(true);
            return this.inputSides.contains(side) && allowFluids;
        }
        return false;
    }

    @Override
    public boolean canDrain(Direction side) {
        if (this.controller == null || this.controller.isAllowedToWork()) {
            boolean allowFluids = Optional.ofNullable(this.coverHandler)
                .flatMap(handler -> handler.getCoverAtSide(side))
                .map(Cover::letsLiquidsOut)
                .orElse(true);
            return this.outputSides.contains(side) && allowFluids;
        }
        return false;
    }
}
