package dev.su5ed.gregtechmod.util.power;

import dev.su5ed.gregtechmod.api.machine.PowerProvider;
import dev.su5ed.gregtechmod.api.util.ChargingSlot;
import dev.su5ed.gregtechmod.api.util.DischargingSlot;
import net.minecraft.core.Direction;

import java.util.Collection;

public interface PowerStorage extends PowerProvider {
    void load();

    void unload();

    void tickServer();

    int getSinkTier();

    int getSourceTier();

    double getMaxInputEUp();

    double getMaxOutputEUp();

    double getMaxOutputEUt();

    int getSourcePackets();

    double getAverageInput();

    double getAverageOutput();

    boolean charge(double amount);

    boolean isSink();

    boolean isSource();

    void overload();

    void overload(float multiplier);

    void addChargingSlot(ChargingSlot slot);

    void addDischargingSlot(DischargingSlot slot);
    
    Collection<Direction> getSinkSides();

    Collection<Direction> getSourceSides();
}
