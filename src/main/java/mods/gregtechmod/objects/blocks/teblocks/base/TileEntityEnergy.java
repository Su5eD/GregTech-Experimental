package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IChargingSlot;
import ic2.api.energy.tile.IDischargingSlot;
import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.core.ExplosionIC2;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.MachineSafety;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TileEntityEnergy extends TileEntityCoverBehavior implements IExplosionPowerOverride, IElectricMachine {
    protected boolean energyCapacityTooltip;
    
    protected AdjustableEnergy energy;
    
    public boolean shouldExplode;
    private boolean explode;
    private float explosionPower;

    public TileEntityEnergy(String descriptionKey) {
        super(descriptionKey);
        this.energy = addComponent(createEnergyComponent());
    }
    
    protected AdjustableEnergy createEnergyComponent() {
        return new DynamicAdjustableEnergy();
    }
    
    @Override
    public abstract int getEUCapacity();
    
    @Override
    public double getStoredEU() { 
        return this.energy.getStoredEnergy();
    }
    
    protected Collection<EnumFacing> getSinkSides() {
        return Collections.emptySet();
    }
    
    protected Collection<EnumFacing> getSourceSides() {
        return Collections.emptySet();
    }
    
    @Override
    public int getSinkTier() {
        return 0;
    }

    @Override
    public int getSourceTier() {
        return 0;
    }
    
    @Override
    public double getMaxInputEUp() {
        return EnergyNet.instance.getPowerFromTier(getSinkTier());
    }
    
    public double getMaxOutputEUp() {
        return EnergyNet.instance.getPowerFromTier(getSourceTier());
    }
    
    @Override
    public final double getMaxOutputEUt() {
        return this.energy.getMaxOutputEUt();
    }
    
    @Override
    public boolean addEnergy(double amount) {
        if (this.energy.isSink() && amount > getMaxInputEUp()) markForExplosion();
        return this.energy.charge(amount);
    }
    
    protected void forceAddEnergy(double amount) {
        this.energy.forceCharge(amount);
    }

    protected int getSourcePackets() {
        return 1;
    }
    
    @Override
    public double getAverageEUInput() {
        return this.energy.getAverageEUInput();
    }
    
    @Override
    public double getAverageEUOutput() {
        return this.energy.getAverageEUOutput();
    }
    
    @Override
    public double useEnergy(double amount, boolean simulate) {
        return this.energy.discharge(amount, simulate);
    }

    @Override
    public boolean tryUseEnergy(double amount, boolean simulate) {
        return useEnergy(amount, simulate) >= amount;
    }

    @Override
    public boolean canUseEnergy(double amount) {
        return getStoredEU() >= amount;
    }
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if(this.explode) this.explodeMachine(this.explosionPower);
        if (this.shouldExplode) this.explode = true; //Extra step so machines don't explode before the packet of death is sent
        if (enableMachineSafety()) MachineSafety.checkSafety(this);
    }
    
    protected void addChargingSlot(IChargingSlot slot) {
        this.energy.addChargingSlot(slot);
    }
    
    protected void addDischargingSlot(IDischargingSlot slot) {
        this.energy.addDischargingSlot(slot);
    }

    protected boolean enableMachineSafety() {
        return true;
    }

    @Override
    public void markForExplosion() {
        markForExplosion(getExplosionPower(Math.max(getSinkTier(), getSourceTier()) + 1, 1.5F));
    }

    @Override
    public void markForExplosion(float power) {
        this.shouldExplode = true;
        this.explosionPower = power;
        if (GregTechConfig.MACHINES.machineWireFire) {
            double energy = getStoredEU();
            this.energy.onUnloaded();
            this.energy = new ExplodingEnergySource(this);
            this.energy.onLoaded();
            this.energy.forceCharge(energy);
        }
    }

    @Override
    public boolean shouldExplode() {
        return true;
    }

    @Override
    public float getExplosionPower(int tier, float defaultPower) {
        return Math.max(defaultPower, tier * GregTechConfig.BALANCE.explosionPowerMultiplier);
    }
    
    public void explodeMachine(float power) {
        int x = this.pos.getX(), y = this.pos.getY(), z = this.pos.getZ();
        this.energy.onUnloaded();
        this.world.setBlockToAir(this.pos);
        new ExplosionIC2(this.world, null, x + 0.5, y + 0.5, z + 0.5, power, 0.5F, ExplosionIC2.Type.Normal).doExplosion();
    }
    
    @Override
    protected boolean isFlammable(EnumFacing face) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        if (this.energy.isSink()) tooltip.add(GtUtil.translateInfo("max_energy_in", Math.round(getMaxInputEUp())));
        if (this.energy.isSource()) tooltip.add(GtUtil.translateInfo("max_energy_out", Math.round(getMaxOutputEUt())));
        if (this.energyCapacityTooltip) tooltip.add(GtUtil.translateInfo("eu_storage", GtUtil.formatNumber(this.energy.getCapacity())));
    }
    
    public class DynamicAdjustableEnergy extends AdjustableEnergy {
        
        public DynamicAdjustableEnergy() {
            super(TileEntityEnergy.this);
        }

        @Override
        public int getCapacity() {
            return TileEntityEnergy.this.getEUCapacity();
        }

        @Override
        public Collection<EnumFacing> getSinkSides() {
            return filterEnergySides(TileEntityEnergy.this.getSinkSides());
        }

        @Override
        public Collection<EnumFacing> getSourceSides() {
            return filterEnergySides(TileEntityEnergy.this.getSourceSides());
        }

        @Override
        public int getSinkTier() {
            return TileEntityEnergy.this.getSinkTier();
        }

        @Override
        public int getSourceTier() {
            return TileEntityEnergy.this.getSourceTier();
        }

        @Override
        public double getMaxOutputEUp() {
            return TileEntityEnergy.this.getMaxOutputEUp();
        }

        @Override
        public int getSourcePackets() {
            return TileEntityEnergy.this.getSourcePackets();
        }
        
        private Collection<EnumFacing> filterEnergySides(Collection<EnumFacing> sides) {
            return sides.stream()
                    .filter(side -> {
                        ICover cover = coverHandler.covers.get(side);
                        return cover == null || cover.allowEnergyTransfer();
                    })
                    .collect(Collectors.toList());
        }
    }
    
    public static class ExplodingEnergySource extends AdjustableEnergy {

        public ExplodingEnergySource(TileEntityBlock parent) {
            super(parent);
        }

        @Override
        public int getCapacity() {
            return 0;
        }

        @Override
        public Collection<EnumFacing> getSinkSides() {
            return Collections.emptySet();
        }

        @Override
        public Collection<EnumFacing> getSourceSides() {
            return Util.allFacings;
        }

        @Override
        public int getSinkTier() {
            return 0;
        }

        @Override
        public int getSourceTier() {
            return 5;
        }

        @Override
        public double getMaxOutputEUp() {
            return 8192;
        }
    }
}
