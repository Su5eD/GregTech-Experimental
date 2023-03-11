package dev.su5ed.gtexperimental.util.power;

import dev.su5ed.gtexperimental.GregTechConfig;
import dev.su5ed.gtexperimental.api.machine.ElectricBlockEntity;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMultiEnergySource;
import ic2.api.tile.IEnergyStorage;
import ic2.core.ExplosionIC2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;

public class IC2EnergyStorage<T extends BaseBlockEntity & ElectricBlockEntity> extends AdjustableEnergyStorage<T> {
    protected DelegateBase delegate;

    private double averageEUIn;
    private double averageEUOut;
    private boolean injectedEnergy;
    private boolean drawnEnergy;

    public boolean shouldExplode;
    private boolean explode;
    private float explosionPower;

    public IC2EnergyStorage(T parent) {
        super(parent);
    }

    public void load() {
        if (this.delegate == null && !this.parent.getLevel().isClientSide) {
            this.delegate = createDelegate();
            if (this.delegate != null) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.delegate));
            }
        }
    }

    public void unload() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.delegate));
            this.delegate = null;
        }
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public int getCapacity() {
        return this.parent.getEnergyCapacity();
    }

    @Override
    public int getSinkTier() {
        return this.parent.getSinkTier();
    }

    @Override
    public int getSourceTier() {
        return this.parent.getSinkTier();
    }

    @Override
    public double getMaxInputEUp() {
        return EnergyNet.instance.getPowerFromTier(getSinkTier());
    }

    @Override
    public double getMaxOutputEUp() {
        return this.parent.getMaxOutputEUp();
    }

    @Override
    public double getAverageInput() {
        return this.averageEUIn;
    }

    @Override
    public double getAverageOutput() {
        return this.averageEUOut;
    }

    @Override
    public void overload() {
        overload(getExplosionPower(Math.max(getSinkTier(), getSourceTier()) + 1, 1.5F));
    }

    @Override
    public void overload(float multiplier) {
        this.shouldExplode = true;
        this.explosionPower = multiplier;
        if (GregTechConfig.COMMON.machineWireFire.get()) {
            // TODO
//            double energy = getStoredEnergy();
//            this.provider.unload();
//            this.provider = new OverloadingEnergyStorage(this.parent);
//            this.provider.load();
//            this.provider.forceCharge(energy);
        }
    }

    @Override
    public void tickServer() {
        if (this.explode) {
            explodeMachine(this.explosionPower);
            return;
        }

        //Extra step so machines don't explode before the packet of death is sent
        if (this.shouldExplode) {
            this.explode = true;
        }

        super.tickServer();

        if (!this.injectedEnergy) {
            updateAverageEUInput(0);
        }
        if (!this.drawnEnergy) {
            updateAverageEUOutput(0);
        }

        if (isSink()) {
            double sum = Arrays.stream(this.averageEUInputs).sum();
            this.averageEUIn = sum / this.averageEUInputs.length;
        }
        else {
            this.averageEUIn = 0;
        }

        if (isSource()) {
            double sum = Arrays.stream(this.averageEUOutputs).sum();
            this.averageEUOut = sum / this.averageEUOutputs.length;
        }
        else {
            this.averageEUOut = 0;
        }

        this.injectedEnergy = false;
        this.drawnEnergy = false;
    }

    private float getExplosionPower(int tier, float defaultPower) {
        return Math.max(defaultPower, tier * GregTechConfig.COMMON.explosionPowerMultiplier.get());
    }

    private void explodeMachine(float power) {
        BlockPos pos = this.parent.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        unload();
        this.parent.getLevel().removeBlock(pos, false);
        new ExplosionIC2(this.parent.getLevel(), null, x + 0.5, y + 0.5, z + 0.5, power, 0.5F, ExplosionIC2.Type.Normal).doExplosion();
    }

    protected DelegateBase createDelegate() {
        boolean sink = !getSinkSides().isEmpty();
        boolean source = !getSourceSides().isEmpty();
        DelegateBase delegate;
        if (sink && source) {
            delegate = new DualDelegate(this.parent);
        }
        else if (sink) {
            delegate = new SinkDelegate(this.parent);
        }
        else if (source) {
            delegate = new SourceDelegate(this.parent);
        }
        else {
            return null;
        }
        delegate.setLevel(this.parent.getLevel());
        return delegate;
    }

    public abstract class DelegateBase extends BlockEntity implements IEnergyTile, IEnergyStorage {
        public DelegateBase(BlockEntity parent) {
            super(parent.getType(), parent.getBlockPos(), parent.getBlockState());
        }

        @Override
        public int getStored() {
            return (int) getStoredEnergy();
        }

        @Override
        public void setStored(int energy) {
            setStoredEnergy(energy);
        }

        @Override
        public int addEnergy(int energy) {
            charge(energy);
            return getStored();
        }

        @Override
        public int getCapacity() {
            return IC2EnergyStorage.this.getCapacity();
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
        public boolean isTeleporterCompatible(Direction side) {
            return isSource() && getMaxOutputEUt() >= 128 && getCapacity() >= 500000;
        }
    }

    public class DualDelegate extends SourceDelegate implements IEnergySink {

        public DualDelegate(BlockEntity parent) {
            super(parent);
        }

        @Override
        public double getDemandedEnergy() {
            int capacity = getCapacity();
            double storedEnergy = getStoredEnergy();
            return isSink() && storedEnergy < capacity ? capacity - storedEnergy : 0;
        }

        @Override
        public int getSinkTier() {
            return IC2EnergyStorage.this.getSinkTier();
        }

        @Override
        public double injectEnergy(Direction enumFacing, double amount, double voltage) {
            return amount - IC2EnergyStorage.this.injectEnergy(amount);
        }

        @Override
        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction side) {
            return getActualSinkSides().contains(side);
        }
    }

    public class SinkDelegate extends DelegateBase implements IEnergySink {

        public SinkDelegate(BlockEntity parent) {
            super(parent);
        }

        @Override
        public int getSinkTier() {
            return IC2EnergyStorage.this.getSinkTier();
        }

        @Override
        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction side) {
            return getActualSinkSides().contains(side);
        }

        @Override
        public double getDemandedEnergy() {
            int capacity = getCapacity();
            double storedEnergy = getStoredEnergy();
            return isSink() && storedEnergy < capacity ? capacity - storedEnergy : 0;
        }

        @Override
        public double injectEnergy(Direction directionFrom, double amount, double voltage) {
            return amount - IC2EnergyStorage.this.injectEnergy(amount);
        }
    }

    public class SourceDelegate extends DelegateBase implements IMultiEnergySource {

        public SourceDelegate(BlockEntity parent) {
            super(parent);
        }

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
            double minOutput = getMaxOutputEUp() + parent.getMinimumStoredEnergy();
            return IC2EnergyStorage.this.getStoredEnergy() >= minOutput ? IC2EnergyStorage.this.getOfferedEnergy() : 0;
        }

        @Override
        public void drawEnergy(double amount) {
            if (useEnergy(amount) >= amount) {
                drawnEnergy = true;
                updateAverageEUOutput(amount);
            }
        }

        @Override
        public int getSourceTier() {
            return IC2EnergyStorage.this.getSourceTier();
        }

        @Override
        public boolean emitsEnergyTo(IEnergyAcceptor acceptor, Direction side) {
            return getActualSourceSides().contains(side);
        }
    }
}
