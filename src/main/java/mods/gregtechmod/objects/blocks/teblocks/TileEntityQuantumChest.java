package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityDigitalChestBase;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityQuantumChest extends TileEntityDigitalChestBase {

    public TileEntityQuantumChest() {
        super("quantum_chest", GregTechConfig.FEATURES.quantumChestMaxItemCount);
    }
    
    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return EnumSet.of(GtUpgradeType.LOCK);
    }
}
