package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.*;
import ic2.api.tile.IEnergyStorage;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.network.GrowingBuffer;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class AdjustableEnergy extends TileEntityComponent {
    private DelegateBase delegate;
    
    private int sinkTier;
    private int sourceTier;
    private double maxOutput;
    private int sourcePackets = 1;
    
    private double capacity;
    private double storedEnergy;
    private Collection<EnumFacing> sinkSides;
    private Collection<EnumFacing> sourceSides;
    
    private final Collection<IChargingSlot> chargingSlots = new HashSet<>();
    private final Collection<IDischargingSlot> dischargingSlots = new HashSet<>();

    public AdjustableEnergy(TileEntityBlock parent, double capacity, int sinkTier, int sourceTier, double maxOutput, Collection<EnumFacing> sinkSides, Collection<EnumFacing> sourceSides) {
        super(parent);
        this.capacity = capacity;
        this.sinkTier = sinkTier;
        this.sourceTier = sourceTier;
        this.maxOutput = maxOutput;
        this.sinkSides = sinkSides;
        this.sourceSides = sourceSides;
    }
    
    public static AdjustableEnergy createSink(TileEntityBlock parent, double capacity, int tier, Collection<EnumFacing> sides) {
        return new AdjustableEnergy(parent, capacity, tier, 0, -1, Collections.unmodifiableCollection(sides), Collections.emptySet());
    }
    
    public static AdjustableEnergy createSource(TileEntityBlock parent, double capacity, int tier, Collection<EnumFacing> sides) {
        return createSource(parent, capacity, tier, -1, sides);
    }
    
    public static AdjustableEnergy createSource(TileEntityBlock parent, double capacity, int tier, double maxOutput, Collection<EnumFacing> sides) {
        return new AdjustableEnergy(parent, capacity, 0, tier, maxOutput, Collections.emptySet(), Collections.unmodifiableCollection(sides));
    }
    
    public boolean charge(double amount) {
        double charged = Math.min(this.capacity - this.storedEnergy, amount);
        this.storedEnergy += charged;
        return charged >= amount;
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
    
    public void addChargingSlot(IChargingSlot slot) {
        this.chargingSlots.add(slot);
    }
    
    public void addDischargingSlot(IDischargingSlot slot) {
        this.dischargingSlots.add(slot);
    }

    public int getSinkTier() {
        return this.sinkTier;
    }

    public void setSinkTier(int sinkTier) {
        this.sinkTier = sinkTier;
    }

    public int getSourceTier() {
        return this.sourceTier;
    }

    public void setSourceTier(int sourceTier) {
        this.sourceTier = sourceTier;
    }

    public double getStoredEnergy() {
        return this.storedEnergy;
    }

    public double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getMaxOutputEUp() {
        return maxOutput < 0 ? EnergyNet.instance.getPowerFromTier(sourceTier) : maxOutput;
    }

    public void setMaxOutput(double maxOutput) {
        this.maxOutput = maxOutput;
    }

    public int getSourcePackets() {
        return sourcePackets;
    }

    public void setSourcePackets(int sourcePackets) {
        this.sourcePackets = sourcePackets;
    }

    public Collection<EnumFacing> getSinkSides() {
        return this.sinkSides;
    }

    public Collection<EnumFacing> getSourceSides() {
        return this.sourceSides;
    }
    
    public void setSides(Collection<EnumFacing> sinkSides, Collection<EnumFacing> sourceSides) {
        boolean reload = !(GtUtil.matchCollections(this.sinkSides, sinkSides) && GtUtil.matchCollections(this.sourceSides, sourceSides));
        
        this.sinkSides = Collections.unmodifiableCollection(sinkSides);
        this.sourceSides = Collections.unmodifiableCollection(sourceSides);
        
        if (reload) {
            this.onUnloaded();
            this.onLoaded();
        }
    }
    
    public boolean isSink() {
        return !this.sinkSides.isEmpty();
    }
    
    public boolean isSource() {
        return !this.sourceSides.isEmpty();
    }
    
    @Override
    public void onLoaded() {
        if (this.delegate == null && !this.parent.getWorld().isRemote) {
            this.delegate = constructDelegate();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.delegate));
        }
    }
    
    @Override
    public void onUnloaded() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.delegate));
            this.delegate = null;
        }
    }
    
    private DelegateBase constructDelegate() {
        DelegateBase delegate;
        
        if (!this.sinkSides.isEmpty() && !this.sourceSides.isEmpty()) {
            delegate = new DualDelegate();
        } else if (!this.sinkSides.isEmpty()) {
            delegate = new SinkDelegate();
        } else if (!this.sourceSides.isEmpty()) {
            delegate = new SourceDelegate();
        } else delegate = null;
            
        if (delegate != null) {
            delegate.setWorld(this.parent.getWorld());
            delegate.setPos(this.parent.getPos());
            
            return delegate;
        }
        
        return null;
    }
    
    @Override
    public void onContainerUpdate(EntityPlayerMP player) {
        GrowingBuffer buf = new GrowingBuffer(16);
        buf.writeDouble(this.capacity);
        buf.writeDouble(this.storedEnergy);
        buf.flip();
                            
        setNetworkUpdate(player, buf);
    }
    
    @Override
    public void onNetworkUpdate(DataInput in) throws IOException {
        this.capacity = in.readDouble();
        this.storedEnergy = in.readDouble();
    }
    
    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        this.storedEnergy = nbt.getDouble("storedEnergy");
    }
    
    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storedEnergy", this.storedEnergy);
        return ret;
    }

    @Override
    public boolean enableWorldTick() {
        return !parent.getWorld().isRemote && !(this.chargingSlots.isEmpty() && this.dischargingSlots.isEmpty());
    }

    @Override
    public void onWorldTick() {
        chargingSlots
                .forEach(slot -> {
                    if (this.storedEnergy > 0) this.discharge(slot.charge(this.storedEnergy));
                });
        dischargingSlots
                .forEach(slot -> {
                    double space = this.capacity - this.storedEnergy;
                    if (space > 0) this.charge(slot.discharge(space, false));
                });
    }

    private abstract class DelegateBase extends TileEntity implements IEnergyTile, IEnergyStorage {
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
            return (int) AdjustableEnergy.this.getCapacity();
        }

        @Override
        public int getOutput() {
            return (int) getOutputEnergyUnitsPerTick();
        }

        @Override
        public double getOutputEnergyUnitsPerTick() {
            return getMaxOutputEUp() * getSourcePackets();
        }

        @Override
        public boolean isTeleporterCompatible(EnumFacing side) {
            return isSource() && getMaxOutputEUp() >= 128 && getCapacity() >= 500000;
        }
    }
    
    private class DualDelegate extends SourceDelegate implements IEnergySink {
        @Override
        public double getDemandedEnergy() {
            return !sinkSides.isEmpty() && storedEnergy < capacity ? capacity - storedEnergy : 0;
        }

        @Override
        public int getSinkTier() {
            return sinkTier;
        }

        @Override
        public double injectEnergy(EnumFacing enumFacing, double amount, double voltage) {
            double injected = Math.min(capacity - storedEnergy, amount);
            storedEnergy += injected;
            return amount - injected;
        }

        @Override
        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
            return sinkSides.contains(side);
        }
    }
    
    private class SinkDelegate extends DelegateBase implements IEnergySink {
    
        @Override
        public int getSinkTier() {
            return sinkTier;
        }
    
        @Override
        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
            return sinkSides.contains(side);
        }
    
        @Override
        public double getDemandedEnergy() {
            return !sinkSides.isEmpty() && storedEnergy < capacity ? capacity - storedEnergy : 0;
        }
    
        @Override
        public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
            double injected = Math.min(capacity - storedEnergy, amount);
            storedEnergy += injected;
            return amount - injected;
        }
    }
    
    public class SourceDelegate extends DelegateBase implements IMultiEnergySource {
        @Override
        public boolean sendMultipleEnergyPackets() {
            return sourcePackets > 1;
        }

        @Override
        public int getMultipleEnergyPacketAmount() {
            return sourcePackets;
        }

        @Override
        public double getOfferedEnergy() {
            return storedEnergy;
        }
        
        public double getMaxOutputEUp() { // Exposes method to public access
            return AdjustableEnergy.this.getMaxOutputEUp();
        }

        @Override
        public void drawEnergy(double amount) {
            if (amount <= storedEnergy) storedEnergy -= amount;
        }

        @Override
        public int getSourceTier() {
            return sourceTier;
        }

        @Override
        public boolean emitsEnergyTo(IEnergyAcceptor acceptor, EnumFacing side) {
            return sourceSides.contains(side);
        }
    }
}
