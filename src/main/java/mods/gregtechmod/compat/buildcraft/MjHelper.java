package mods.gregtechmod.compat.buildcraft;

import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjReceiver;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class MjHelper {
    public static final long MJ = 1_000_000L;

    @Nullable
    @CapabilityInject(IMjReceiver.class)
    public static Capability<IMjReceiver> RECEIVER_CAPABILITY;

    @Nullable
    @CapabilityInject(IMjConnector.class)
    public static Capability<IMjReceiver> CONNECTOR_CAPABILITY;

    public static long microJoules(double joules) {
        return (long) (joules * MJ);
    }
    
    public static double joules(long microJoules) {
        return microJoules / (double) MJ;
    }
    
    public static double toEU(long microJoules) {
        double mj = microJoules / (double) MJ;
        return mj * 2.5;
    }
}
