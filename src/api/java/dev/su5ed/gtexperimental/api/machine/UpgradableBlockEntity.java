package dev.su5ed.gtexperimental.api.machine;

import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.api.util.GtFluidTank;

import java.util.Set;

/**
 * Provides upgrade stack compatibility
 *
 * @author Su5eD
 */
public interface UpgradableBlockEntity extends ElectricBlockEntity, HasOwner {
    <T extends GtFluidTank> T addTank(T tank);

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
