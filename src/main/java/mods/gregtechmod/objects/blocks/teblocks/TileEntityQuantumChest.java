package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityDigitalChestBase;

import java.util.Collections;
import java.util.Set;

public class TileEntityQuantumChest extends TileEntityDigitalChestBase {

    public TileEntityQuantumChest() {
        super(GregTechConfig.FEATURES.quantumChestMaxItemCount);
    }
    
    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.singleton(GtUpgradeType.LOCK);
    }
}
