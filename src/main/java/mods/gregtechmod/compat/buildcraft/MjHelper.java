package mods.gregtechmod.compat.buildcraft;

import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjReceiver;
import mods.gregtechmod.compat.ModHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;

public class MjHelper {
    public static final long MJ = 1_000_000L;

    @CapabilityInject(IMjReceiver.class)
    @Nullable
    public static Capability<IMjReceiver> RECEIVER_CAPABILITY;

    @CapabilityInject(IMjConnector.class)
    @Nullable
    public static Capability<IMjReceiver> CONNECTOR_CAPABILITY;

    public static IMjEnergyStorage getMjEnergyStorage(long capacity, long maxReceive, long maxExtract) {
        return ModHandler.buildcraftLib ? _getMjEnergyStorage(capacity, maxReceive, maxExtract) : null;
    }

    @Optional.Method(modid = "buildcraftlib")
    private static IMjEnergyStorage _getMjEnergyStorage(long capacity, long maxReceive, long maxExtract) {
        return new MjReceiverWrapper(capacity, maxReceive, maxExtract);
    }

    public static long convert(double amount) {
        return (long) (amount * MJ);
    }
}
