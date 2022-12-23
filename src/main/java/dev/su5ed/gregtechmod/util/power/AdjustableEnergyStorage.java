package dev.su5ed.gregtechmod.util.power;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.machine.ElectricBlockEntity;
import dev.su5ed.gregtechmod.api.util.ChargingSlot;
import dev.su5ed.gregtechmod.api.util.DischargingSlot;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.JavaUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.HashSet;

public abstract class AdjustableEnergyStorage<T extends BaseBlockEntity & ElectricBlockEntity> implements PowerStorage, INBTSerializable<CompoundTag> {
    protected final T parent;
    private final CoverHandler coverHandler;

    private final Collection<ChargingSlot> chargingSlots = new HashSet<>();
    private final Collection<DischargingSlot> dischargingSlots = new HashSet<>();

    private double storedEnergy;
    private Collection<Direction> oldSinkSides;
    private Collection<Direction> oldSourceSides;

    protected double[] averageEUInputs = new double[5];
    protected double[] averageEUOutputs = new double[5];
    protected int averageEUInputIndex = 0;
    protected int averageEUOutputIndex = 0;

    protected AdjustableEnergyStorage(T parent) {
        this.parent = parent;
        this.coverHandler = parent.getCapability(Capabilities.COVER_HANDLER).orElse(null);
    }

    @Override
    public void load() {}

    @Override
    public void unload() {}

    public void addChargingSlot(ChargingSlot slot) {
        this.chargingSlots.add(slot);
    }

    public void addDischargingSlot(DischargingSlot slot) {
        this.dischargingSlots.add(slot);
    }

    @Override
    public double getStoredEnergy() {
        return this.storedEnergy;
    }

    @Override
    public double getMaxOutputEUt() {
        return getMaxOutputEUp() * getSourcePackets();
    }

    @Override
    public int getSourcePackets() {
        return 1;
    }

    @Override
    public boolean charge(double amount) {
        return injectEnergy(amount) >= amount;
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        if (this.storedEnergy >= amount) {
            if (!simulate) {
                this.storedEnergy -= amount;
            }
            return amount;
        }
        return 0;
    }

    @Override
    public boolean isSink() {
        return !getActualSinkSides().isEmpty();
    }

    @Override
    public boolean isSource() {
        return !getActualSourceSides().isEmpty();
    }

    @Override
    public void overload() {}

    @Override
    public void overload(float multiplier) {}

    @Override
    public void tickServer() {
        if (this.chargingSlots.isEmpty() && this.dischargingSlots.isEmpty()) {
            return;
        }

        this.chargingSlots
            .forEach(slot -> {
                double storedEnergy = getStoredEnergy();
                if (storedEnergy > 0) {
                    useEnergy(slot.charge(storedEnergy));
                }
            });
        this.dischargingSlots
            .forEach(slot -> {
                double space = getCapacity() - getStoredEnergy();

                if (space > 0) {
                    double energy = slot.discharge(space, false);
                    if (energy > 0) {
                        charge(energy);
                    }
                }
            });
    }

    @Override
    public Collection<Direction> getSinkSides() {
        return filterEnergySides(this.parent.getSinkSides());
    }

    @Override
    public Collection<Direction> getSourceSides() {
        return filterEnergySides(this.parent.getSourceSides());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("storedEnergy", this.storedEnergy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.storedEnergy = nbt.getInt("storedEnergy");
    }

    protected void setStoredEnergy(double storedEnergy) {
        this.storedEnergy = storedEnergy;
    }

    protected double getOfferedEnergy() {
        return Math.min(getStoredEnergy(), getMaxOutputEUt());
    }

    protected void updateAverageEUInput(double amount) {
        this.averageEUInputIndex = ++this.averageEUInputIndex % this.averageEUInputs.length;
        this.averageEUInputs[this.averageEUInputIndex] = amount;
    }

    protected void updateAverageEUOutput(double amount) {
        this.averageEUOutputIndex = ++this.averageEUOutputIndex % this.averageEUOutputs.length;
        this.averageEUOutputs[this.averageEUOutputIndex] = amount;
    }

    protected double injectEnergy(double amount) {
        double injected = Math.min(getCapacity() - this.storedEnergy, amount);
        this.storedEnergy += injected;

        updateAverageEUInput(injected);

        return injected;
    }


    protected Collection<Direction> getActualSinkSides() {
        return refreshSides().getLeft();
    }

    protected Collection<Direction> getActualSourceSides() {
        return refreshSides().getRight();
    }

    public Pair<Collection<Direction>, Collection<Direction>> refreshSides() {
        Collection<Direction> sinkSides = getSinkSides();
        Collection<Direction> sourceSides = getSourceSides();

        refreshSides(sinkSides, getOldSinkSides(sinkSides), sourceSides, getOldSourceSides(sourceSides));

        return Pair.of(sinkSides, sourceSides);
    }

    private void refreshSides(Collection<Direction> sinkSides, Collection<Direction> oldSinkSides, Collection<Direction> sourceSides, Collection<Direction> oldSourceSides) {
        boolean reload = !JavaUtil.matchCollections(sinkSides, oldSinkSides) || !JavaUtil.matchCollections(sourceSides, oldSourceSides);
        this.oldSinkSides = sinkSides;
        this.oldSourceSides = sourceSides;

        if (reload) {
            unload();
            load();

            this.parent.updateRenderNeighbors();
        }
    }

    private Collection<Direction> getOldSinkSides(Collection<Direction> sinkSides) {
        if (this.oldSinkSides == null) {
            this.oldSinkSides = sinkSides;
        }
        return this.oldSinkSides;
    }

    private Collection<Direction> getOldSourceSides(Collection<Direction> sourceSides) {
        if (this.oldSourceSides == null) {
            this.oldSourceSides = sourceSides;
        }
        return this.oldSourceSides;
    }

    private Collection<Direction> filterEnergySides(Collection<Direction> sides) {
        return this.coverHandler == null ? sides : sides.stream()
            .filter(side -> this.coverHandler.getCoverAtSide(side)
                .map(Cover::allowEnergyTransfer)
                .orElse(true))
            .toList();
    }
}
