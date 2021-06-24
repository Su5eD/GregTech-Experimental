package mods.gregtechmod.objects.blocks.teblocks.generator;

import ic2.api.energy.EnergyNet;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGenerator;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class TileEntityDragonEggEnergySiphon extends TileEntityGenerator implements IPanelInfoProvider {
    private static TileEntityDragonEggEnergySiphon activeSiphon = null;

    // TODO Add recipe when the supercondensator is added
    public TileEntityDragonEggEnergySiphon() {
        super("dragon_egg_energy_siphon");
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (isAllowedToWork() && hasEgg()) {
            setActive(true);
            
            if (this.energy.charge(GregTechConfig.MACHINES.dragonEggEnergySiphon.dragonEggEnergyPerTick) 
                    && GregTechConfig.MACHINES.dragonEggEnergySiphon.outputFlux 
                    && GtUtil.RANDOM.nextInt(1000) == 5) {
                ModHandler.polluteAura(this.world, this.pos, 4, true);
            }
            
            if (activeSiphon != this && !GregTechConfig.MACHINES.dragonEggEnergySiphon.allowMultipleEggs) {
                if (activeSiphon == null || activeSiphon.isInvalid() || !activeSiphon.hasEgg()) {
                    activeSiphon = this;
                }
                else {
                    markForExplosion(10);
                }
            }
        }
        else if (activeSiphon == this) activeSiphon = null;
        else setActive(false);
    }

    public boolean hasEgg() {
        return this.world.getBlockState(this.pos.offset(EnumFacing.UP)).getBlock() == Blocks.DRAGON_EGG;
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate) {
        if (side == EnumFacing.UP) return false;
        return super.placeCoverAtSide(cover, side, simulate);
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return AdjustableEnergy.createSource(this, 512 + GregTechConfig.MACHINES.dragonEggEnergySiphon.dragonEggEnergyPerTick * 2, EnergyNet.instance.getTierFromPower(GregTechConfig.MACHINES.dragonEggEnergySiphon.dragonEggEnergyPerTick), getSourceSides());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return GtUtil.translateInfo(getActive() ? "active" : "inactive");
    }

    @Override
    public String getSecondaryInfo() {
        return GtUtil.translateInfo("output", GregTechConfig.MACHINES.dragonEggEnergySiphon.dragonEggEnergyPerTick);
    }

    @Override
    public String getTertiaryInfo() {
        return "";
    }
}
