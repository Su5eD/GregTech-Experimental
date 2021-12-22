package mods.gregtechmod.objects.blocks.teblocks.energy;

import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class TileEntitySupercondensator extends TileEntityUpgradable {

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return GtUtil.allSidesExcept(getFacing());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return Collections.singleton(getFacing());
    }

    @Override
    public double getMaxInputEUp() {
        return 1000000;
    }

    @Override
    public double getMaxOutputEUp() {
        return isAllowedToWork() ? 1000000 : 0;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.singleton(GtUpgradeType.BATTERY);
    }

    @Override
    public int getBaseSinkTier() {
        return 9;
    }

    @Override
    public int getBaseSourceTier() {
        return 9;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000000;
    }
}
