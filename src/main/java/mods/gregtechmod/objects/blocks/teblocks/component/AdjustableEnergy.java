package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.*;
import ic2.api.tile.IEnergyStorage;
import ic2.core.network.GrowingBuffer;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public abstract class AdjustableEnergy extends GtComponentBase {
    protected DelegateBase delegate;

    @NBTPersistent
    private double storedEnergy;
    private Collection<EnumFacing> oldSinkSides;
    private Collection<EnumFacing> oldSourceSides;

    private final Collection<IChargingSlot> chargingSlots = new HashSet<>();
    private final Collection<IDischargingSlot> dischargingSlots = new HashSet<>();

    protected double[] averageEUInputs = new double[] { 0, 0, 0, 0, 0 };
    protected double[] averageEUOutputs = new double[] { 0, 0, 0, 0, 0 };
    protected int averageEUInputIndex = 0;
    protected int averageEUOutputIndex = 0;
    private double averageEUIn;
    private double averageEUOut;
    private boolean injectedEnergy;
    private boolean drawnEnergy;

    public AdjustableEnergy(TileEntityAutoNBT parent) {
        super(parent);
    }

    public abstract int getCapacity();

    public double getStoredEnergy() {
        return this.storedEnergy;
    }

    public abstract Collection<EnumFacing> getSinkSides();

    public abstract Collection<EnumFacing> getSourceSides();

    public abstract int getSinkTier();

    public abstract int getSourceTier();

    public abstract double getMaxOutputEUp();

    public double getMaxOutputEUt() {
        return getMaxOutputEUp() * getSourcePackets();
    }

    public int getSourcePackets() {
        return 1;
    }

    protected double getOfferedEnergy() {
        return Math.min(getStoredEnergy(), getMaxOutputEUt());
    }

    public double getAverageEUInput() {
        return this.averageEUIn;
    }

    protected void updateAverageEUInput(double amount) {
        this.averageEUInputIndex = ++this.averageEUInputIndex % this.averageEUInputs.length;
        this.averageEUInputs[this.averageEUInputIndex] = amount;
    }

    public double getAverageEUOutput() {
        return this.averageEUOut;
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

    public boolean charge(double amount) {
        return injectEnergy(amount) >= amount;
    }

    public void forceCharge(double amount) {
        this.storedEnergy += amount;
    }

    public double discharge(double amount) {
        return discharge(amount, false);
    }

    public double discharge(double amount, boolean simulate) {
        if (this.storedEnergy >= amount) {
            if (!simulate) this.storedEnergy -= amount;
            return amount;
        }
        return 0;
    }

    public boolean isSink() {
        return !getActualSinkSides().isEmpty();
    }

    public boolean isSource() {
        return !getActualSourceSides().isEmpty();
    }

    public void addChargingSlot(IChargingSlot slot) {
        this.chargingSlots.add(slot);
    }

    public void addDischargingSlot(IDischargingSlot slot) {
        this.dischargingSlots.add(slot);
    }

    private Collection<EnumFacing> getActualSinkSides() {
        return refreshSides().getLeft();
    }

    private Collection<EnumFacing> getActualSourceSides() {
        return refreshSides().getRight();
    }

    public Pair<Collection<EnumFacing>, Collection<EnumFacing>> refreshSides() {
        Collection<EnumFacing> sinkSides = getSinkSides();
        Collection<EnumFacing> sourceSides = getSourceSides();

        refreshSides(sinkSides, getOldSinkSides(sinkSides), sourceSides, getOldSourceSides(sourceSides));

        return Pair.of(sinkSides, sourceSides);
    }

    private void refreshSides(Collection<EnumFacing> sinkSides, Collection<EnumFacing> oldSinkSides, Collection<EnumFacing> sourceSides, Collection<EnumFacing> oldSourceSides) {
        boolean reload = !JavaUtil.matchCollections(sinkSides, oldSinkSides) || !JavaUtil.matchCollections(sourceSides, oldSourceSides);
        this.oldSinkSides = sinkSides;
        this.oldSourceSides = sourceSides;

        if (reload) {
            this.onUnloaded();
            this.onLoaded();

            ((TileEntityAutoNBT) this.parent).updateRenderNeighbors();
        }
    }

    private Collection<EnumFacing> getOldSinkSides(Collection<EnumFacing> sinkSides) {
        if (this.oldSinkSides == null) this.oldSinkSides = sinkSides;
        return this.oldSinkSides;
    }

    private Collection<EnumFacing> getOldSourceSides(Collection<EnumFacing> sourceSides) {
        if (this.oldSourceSides == null) this.oldSourceSides = sourceSides;
        return this.oldSourceSides;
    }

    @Override
    public void onLoaded() {
        if (this.delegate == null && !this.parent.getWorld().isRemote) {
            this.delegate = initDelegate();
            if (this.delegate != null) MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.delegate));
        }
    }

    @Override
    public void onUnloaded() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.delegate));
            this.delegate = null;
        }
    }

    protected DelegateBase createDelegate() {
        boolean sink = !getSinkSides().isEmpty();
        boolean source = !getSourceSides().isEmpty();
        if (sink && source) {
            return new DualDelegate();
        }
        else if (sink) {
            return new SinkDelegate();
        }
        else if (source) {
            return new SourceDelegate();
        }
        else return null;
    }

    private DelegateBase initDelegate() {
        DelegateBase delegate = createDelegate();
        if (delegate != null) {
            delegate.setWorld(this.parent.getWorld());
            delegate.setPos(this.parent.getPos());
        }
        return delegate;
    }

    @Override
    public boolean enableWorldTick() {
        return !this.parent.getWorld().isRemote && !(this.chargingSlots.isEmpty() && this.dischargingSlots.isEmpty());
    }

    @Override
    public void onWorldTick() {
        this.chargingSlots
            .forEach(slot -> {
                double storedEnergy = getStoredEnergy();
                if (storedEnergy > 0) this.discharge(slot.charge(storedEnergy));
            });
        this.dischargingSlots
            .forEach(slot -> {
                double space = getCapacity() - getStoredEnergy();

                if (space > 0) {
                    double energy = slot.discharge(space, false);
                    if (energy > 0) this.charge(energy);
                }
            });
        if (!this.parent.getWorld().isRemote) {
            if (!this.injectedEnergy) updateAverageEUInput(0);
            if (!this.drawnEnergy) updateAverageEUOutput(0);

            if (isSink()) {
                double sum = Arrays.stream(this.averageEUInputs).sum();
                this.averageEUIn = sum / this.averageEUInputs.length;
            }
            else this.averageEUIn = 0;

            if (isSource()) {
                double sum = Arrays.stream(this.averageEUOutputs).sum();
                this.averageEUOut = sum / this.averageEUOutputs.length;
            }
            else averageEUOut = 0;

            this.injectedEnergy = false;
            this.drawnEnergy = false;
        }
    }

    @Override
    public void onContainerUpdate(EntityPlayerMP player) {
        GrowingBuffer buf = new GrowingBuffer(8);
        buf.writeDouble(this.storedEnergy);
        buf.flip();

        setNetworkUpdate(player, buf);
    }

    @Override
    public void onNetworkUpdate(DataInput in) throws IOException {
        this.storedEnergy = in.readDouble();
    }

    public abstract class DelegateBase extends TileEntity implements IEnergyTile, IEnergyStorage {
        @Override
        public int getStored() {
            return (int) getStoredEnergy();
        }

        @Override
        public void setStored(int energy) {
            storedEnergy = energy;
        }

        @Override
        public int addEnergy(int energy) {
            charge(energy);
            return getStored();
        }

        @Override
        public int getCapacity() {
            return AdjustableEnergy.this.getCapacity();
        }

        @Override
        public int getOutput() {
            return (int) getOutputEnergyUnitsPerTick();
        }

        @Override
        public double getOutputEnergyUnitsPerTick() {
            return getMaxOutputEUt();
        }

        @Override
        public boolean isTeleporterCompatible(EnumFacing side) {
            return isSource() && getMaxOutputEUt() >= 128 && getCapacity() >= 500000;
        }
    }

    public class DualDelegate extends SourceDelegate implements IEnergySink {
        @Override
        public double getDemandedEnergy() {
            int capacity = getCapacity();
            double storedEnergy = getStoredEnergy();
            return isSink() && storedEnergy < capacity ? capacity - storedEnergy : 0;
        }

        @Override
        public int getSinkTier() {
            return AdjustableEnergy.this.getSinkTier();
        }

        @Override
        public double injectEnergy(EnumFacing enumFacing, double amount, double voltage) {
            return amount - AdjustableEnergy.this.injectEnergy(amount);
        }

        @Override
        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
            return getActualSinkSides().contains(side);
        }
    }

    public class SinkDelegate extends DelegateBase implements IEnergySink {

        @Override
        public int getSinkTier() {
            return AdjustableEnergy.this.getSinkTier();
        }

        @Override
        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
            return getActualSinkSides().contains(side);
        }

        @Override
        public double getDemandedEnergy() {
            int capacity = getCapacity();
            double storedEnergy = getStoredEnergy();
            return isSink() && storedEnergy < capacity ? capacity - storedEnergy : 0;
        }

        @Override
        public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
            return amount - AdjustableEnergy.this.injectEnergy(amount);
        }
    }

    public class SourceDelegate extends DelegateBase implements IMultiEnergySource {
        @Override
        public boolean sendMultipleEnergyPackets() {
            return getSourcePackets() > 1;
        }

        @Override
        public int getMultipleEnergyPacketAmount() {
            return getSourcePackets();
        }

        @Override
        public double getOfferedEnergy() {
            return AdjustableEnergy.this.getOfferedEnergy();
        }

        public double getMaxOutputEUp() { // Exposes method to public access
            return AdjustableEnergy.this.getMaxOutputEUp();
        }

        @Override
        public void drawEnergy(double amount) {
            if (discharge(amount) >= amount) {
                drawnEnergy = true;
                updateAverageEUOutput(amount);
            }
        }

        @Override
        public int getSourceTier() {
            return AdjustableEnergy.this.getSourceTier();
        }

        @Override
        public boolean emitsEnergyTo(IEnergyAcceptor acceptor, EnumFacing side) {
            return getActualSourceSides().contains(side);
        }
    }
}
