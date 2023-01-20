package dev.su5ed.gtexperimental.util.capability;

import net.minecraft.nbt.CompoundTag;

public class JumpChargeStore implements JumpCharge {
    private double charge;

    public JumpChargeStore(double initialCharge) {
        this.charge = initialCharge;
    }

    @Override
    public double getCharge() {
        return this.charge;
    }

    @Override
    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("charge", this.charge);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.charge = nbt.getDouble("charge");
    }
}
