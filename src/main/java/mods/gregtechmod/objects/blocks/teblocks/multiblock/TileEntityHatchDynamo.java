package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.api.energy.EnergyNet;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TileEntityHatchDynamo extends TileEntityEnergy {

    public TileEntityHatchDynamo() {
        super("hatch_dynamo");
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.set(2, GtUtil.translate("teblock.hatch_dynamo.description2"));
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        return this.energy.discharge(amount, simulate);
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return new DynamoAdjustableEnergy();
    }
    
    private class DynamoAdjustableEnergy extends AdjustableEnergy {
        private static final double MINIMUM_STORED_ENERGY = 512;
        
        public DynamoAdjustableEnergy() {
            super(TileEntityHatchDynamo.this, 8192 + MINIMUM_STORED_ENERGY, 0, 1, -1, Collections.emptySet(), Collections.emptySet());
        }

        @Override
        public int getSourceTier() {
            return EnergyNet.instance.getTierFromPower(getMaxOutputEUp());
        }

        @Override
        public double getMaxOutputEUp() {
            return Math.max(0, Math.min(getStoredEnergy() - MINIMUM_STORED_ENERGY, 2048));
        }

        @Override
        public Collection<EnumFacing> getSourceSides() {
            return Collections.singleton(getFacing());
        }
    }
}
