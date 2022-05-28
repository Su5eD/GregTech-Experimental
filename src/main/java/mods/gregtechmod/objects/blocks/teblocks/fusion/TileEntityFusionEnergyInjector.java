package mods.gregtechmod.objects.blocks.teblocks.fusion;

import ic2.core.util.Util;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class TileEntityFusionEnergyInjector extends TileEntityUpgradable {

    public TileEntityFusionEnergyInjector() {
        this.energyCapacityTooltip = true;
    }

    @Override
    public int getBaseSinkTier() {
        return 5;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000000;
    }

    @Override
    public long getMjCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    public int getSteamCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return EnumSet.of(GtUpgradeType.MJ, GtUpgradeType.STEAM);
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }
}
