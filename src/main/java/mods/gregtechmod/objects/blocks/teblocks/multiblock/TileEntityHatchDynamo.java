package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.api.energy.EnergyNet;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.List;

public class TileEntityHatchDynamo extends TileEntityEnergy {
    private static final int MINIMUM_STORED_ENERGY = 512;

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.set(2, GtLocale.translateTeBlock(this, "description2"));
    }

    @Override
    public int getEUCapacity() {
        return 8192 + MINIMUM_STORED_ENERGY;
    }

    @Override
    public int getSourceTier() {
        return Math.max(1, EnergyNet.instance.getTierFromPower(getMaxOutputEUp()));
    }

    @Override
    public double getMaxOutputEUp() {
        return Math.max(0, Math.min(getStoredEU() - MINIMUM_STORED_ENERGY, 2048));
    }

    @Override
    public Collection<EnumFacing> getSourceSides() {
        return facingSideOnly();
    }
}
