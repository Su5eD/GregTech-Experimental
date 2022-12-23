package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.api.util.GtFluidTank;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class FluidHandler extends GtComponentBase<BaseBlockEntity> {
    private static final ResourceLocation NAME = location("fluid_handler");

    private final List<GtFluidTank> tanks;
    private final Map<Direction, LazyOptional<IFluidHandler>> sidedTanks;

    public FluidHandler(BaseBlockEntity parent) {
        super(parent);

        this.tanks = new ArrayList<>();
        this.sidedTanks = StreamEx.of(Direction.values())
            .toMap(side -> LazyOptional.of(() -> new FluidHandlerWrapper(this.tanks, side)));
    }

    public <T extends GtFluidTank> T add(T tank) {
        String name = tank.getName();
        if (this.tanks.stream().map(GtFluidTank::getName).anyMatch(name::equals)) {
            throw new IllegalStateException("Duplicate tank " + name);
        }
        this.tanks.add(tank);
        return tank;
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public void onFieldUpdate(String name) {}

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side != null && !this.tanks.isEmpty()) {
            return this.sidedTanks.get(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        this.sidedTanks.values().forEach(LazyOptional::invalidate);
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);

        CompoundTag tanksTag = new CompoundTag();
        this.tanks.forEach(tank -> tanksTag.put(tank.getName(), tank.save()));
        tag.put("tanks", tanksTag);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);

        CompoundTag tanksTag = tag.getCompound("tanks");
        for (GtFluidTank tank : this.tanks) {
            String name = tank.getName();
            if (tanksTag.contains(name)) {
                CompoundTag tankTag = tanksTag.getCompound(tank.getName());
                tank.load(tankTag);
            }
        }
    }

    private record FluidHandlerWrapper(List<GtFluidTank> tanks, Direction side) implements IFluidHandler {

        @Override
        public int getTanks() {
            return this.tanks.size();
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return this.tanks.get(tank).getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return this.tanks.get(tank).getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return this.tanks.get(tank).isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int total = 0;
            FluidStack remaining = resource.copy();
            for (GtFluidTank tank : this.tanks) {
                if (tank.canFill(this.side)) {
                    int filled = tank.fill(remaining, action);
                    remaining.shrink(filled);
                    total += filled;
                }

                if (total >= resource.getAmount()) {
                    break;
                }
            }
            return total;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack result = new FluidStack(resource.getFluid(), 0);
            for (GtFluidTank tank : this.tanks) {
                if (tank.getFluid().isFluidEqual(resource) && tank.canDrain(this.side)) {
                    int needed = resource.getAmount() - result.getAmount();
                    FluidStack drained = tank.drain(needed, action);
                    result.grow(drained.getAmount());

                    if (result.getAmount() >= resource.getAmount()) {
                        break;
                    }
                }
            }
            return result;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return StreamEx.of(this.tanks)
                .map(tank -> tank.drain(maxDrain, FluidAction.SIMULATE))
                .findFirst(fluid -> !fluid.isEmpty())
                .map(fluid -> {
                    fluid.setAmount(maxDrain);
                    return drain(fluid, action);
                })
                .orElse(FluidStack.EMPTY);
        }
    }
}
