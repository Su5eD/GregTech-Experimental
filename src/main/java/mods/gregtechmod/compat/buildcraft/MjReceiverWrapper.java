package mods.gregtechmod.compat.buildcraft;

import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;

@Optional.Interface(modid = "buildcraftlib", iface = "buildcraft.api.mj.IMjReceiver")
public class MjReceiverWrapper implements IMjEnergyStorage, INBTSerializable<NBTTagCompound>, IMjReceiver {
    private long capacity;
    private long storedEnergy;
    protected long maxReceive;
    protected long maxExtract;

    public MjReceiverWrapper(long capacity, long maxReceive, long maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public long addPower(long microJoulesToAdd, boolean simulate) {
        long energyReceived = Math.min(getCapacity() - getStored(), Math.min(maxReceive, microJoulesToAdd));
        if (!simulate) {
            this.storedEnergy += energyReceived;
        }
        return 0;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong("stored", storedEnergy);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        storedEnergy = nbt.getLong("stored");
    }

    @Override
    public boolean extractPower(long power) {
        if (storedEnergy < power) return false;
        storedEnergy -= power;
        return true;
    }

    @Override
    public long getStored() {
        return storedEnergy;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    @Override
    public long getPowerRequested() {
        return Math.min(100 * MjHelper.MJ, getCapacity() - getStored());
    }

    @Override
    public long receivePower(long microJoules, boolean simulate) {
        return addPower(microJoules, simulate);
    }

    @Override
    public boolean canConnect(@Nonnull IMjConnector other) {
        return true;
    }
}
