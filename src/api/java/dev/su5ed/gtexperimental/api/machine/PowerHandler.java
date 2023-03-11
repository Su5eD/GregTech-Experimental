package dev.su5ed.gtexperimental.api.machine;

import dev.su5ed.gtexperimental.api.util.ChargingSlot;
import dev.su5ed.gtexperimental.api.util.DischargingSlot;
import net.minecraft.core.Direction;

import java.util.Collection;
import java.util.Optional;

public interface PowerHandler {
    void addPowerProvider(PowerProvider provider);

    <T extends PowerProvider> Optional<T> getPowerProvider(Class<T> type);
    
    PowerProvider getPrimaryPowerProvider();

    boolean addEnergy(double amount);

    boolean canUseEnergy(double amount);

    default double useEnergy(double amount) {
        return useEnergy(amount, false);
    }

    double useEnergy(double amount, boolean simulate);

    default boolean tryUseEnergy(double amount) {
        return tryUseEnergy(amount, false);
    }

    boolean tryUseEnergy(double amount, boolean simulate);

    void addChargingSlot(ChargingSlot slot);

    void addDischargingSlot(DischargingSlot slot);

    int getSinkTier();

    int getSourceTier();

    double getMaxInputEUp();

    double getMaxOutputEUt();

    double getMaxOutputEUp();

    double getStoredEnergy();

    int getEnergyCapacity();

    double getAverageInput();

    double getAverageOutput();

    void overload();

    void overload(float power);

    Collection<Direction> getSinkSides();

    Collection<Direction> getSourceSides();
    
    void refreshSides();

    boolean isSink();

    boolean isSource();
}
