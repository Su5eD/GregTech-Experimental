package mods.gregtechmod.objects.blocks.teblocks.generator;

import ic2.api.energy.EnergyNet;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGenerator;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class TileEntityDragonEggEnergySiphon extends TileEntityGenerator implements IPanelInfoProvider {
    private static TileEntityDragonEggEnergySiphon activeSiphon = null;

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.tickCounter == 1) activeSiphon = null;
        
        if (isAllowedToWork() && hasEgg()) {
            setActive(true);
            
            if (activeSiphon != this && !GregTechConfig.MACHINES.DRAGON_EGG_ENERGY_SIPHON.allowMultipleEggs) {
                if (activeSiphon == null || activeSiphon.isInvalid() || !activeSiphon.hasEgg()) {
                    activeSiphon = this;
                }
                else {
                    markForExplosion(10);
                }
            }
            
            if (addEnergy(GregTechConfig.MACHINES.DRAGON_EGG_ENERGY_SIPHON.dragonEggEnergy) 
                    && GregTechConfig.MACHINES.DRAGON_EGG_ENERGY_SIPHON.outputFlux 
                    && this.world.rand.nextInt(1000) == 5) {
                ModHandler.polluteAura(this.world, this.pos, 4, true);
            }
        }
        else if (activeSiphon == this) activeSiphon = null;
        else setActive(false);
    }

    public boolean hasEgg() {
        return this.world.getBlockState(this.pos.offset(EnumFacing.UP)).getBlock() == Blocks.DRAGON_EGG;
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        if (side == EnumFacing.UP) return false;
        return super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public int getSourceTier() {
        return EnergyNet.instance.getTierFromPower(GregTechConfig.MACHINES.DRAGON_EGG_ENERGY_SIPHON.dragonEggEnergy);
    }

    @Override
    protected int getBaseEUCapacity() {
        return getMinimumStoredEnergy() + GregTechConfig.MACHINES.DRAGON_EGG_ENERGY_SIPHON.dragonEggEnergy * 2;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return GtLocale.translateInfo(getActive() ? "active" : "inactive");
    }

    @Override
    public String getSecondaryInfo() {
        return GtLocale.translateInfo("output", GregTechConfig.MACHINES.DRAGON_EGG_ENERGY_SIPHON.dragonEggEnergy);
    }

    @Override
    public String getTertiaryInfo() {
        return "";
    }
}
