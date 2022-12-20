package dev.su5ed.gregtechmod.api.machine;

import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import dev.su5ed.gregtechmod.api.util.GtFluidTank;

import java.util.Optional;
import java.util.Set;

/**
 * Provides upgrade stack compatibility
 *
 * @author Su5eD
 */
public interface UpgradableBlockEntity extends IElectricMachine, HasOwner {
    <T extends GtFluidTank> T addTank(T tank);

    <T extends PowerProvider> Optional<T> getPowerProvider(Class<T> type);

    void addPowerProvider(PowerProvider provider);

    Set<UpgradeCategory> getCompatibleUpgrades();

    int getUpgradeCount(UpgradeCategory category);

    int getExtraEUCapacity();

    default int getBaseSinkTier() {
        return 0;
    }

    default int getBaseSourceTier() {
        return 0;
    }

    default int getBaseSourcePackets() {
        return 1;
    }
}
