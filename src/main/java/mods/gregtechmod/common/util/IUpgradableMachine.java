package mods.gregtechmod.common.util;

import ic2.core.item.upgrade.ItemUpgradeModule;
import mods.gregtechmod.common.objects.items.GtUpgradeItem;

import java.util.Set;

/**
 * Provides upgrade item compatibility
 * @author Su5eD
 */
public interface IUpgradableMachine {

    Set<GtUpgradeItem.GtUpgradeType> getCompatibleGtUpgrades();

    Set<ItemUpgradeModule.UpgradeType> getCompatibleIC2Upgrades();
}
