package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.util.Util;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class TileEntityGenerator extends TileEntityUpgradable {
    
    public TileEntityGenerator(String descriptionKey) {
        super(descriptionKey);
        this.energyCapacityTooltip = true;
    }

    protected boolean canAddEnergy() {
        return getStoredEU() < getMaxOutputEUt() * 10 + getMinimumStoredEnergy();
    }
    
    protected int getMinimumStoredEnergy() {
        return 512;
    }

    @Override
    protected int getBaseSinkTier() {
        return 0;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Collections.emptySet();
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return Util.allFacings;
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.singleton(GtUpgradeType.LOCK);
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }
}
