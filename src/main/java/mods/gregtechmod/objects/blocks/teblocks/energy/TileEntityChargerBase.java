package mods.gregtechmod.objects.blocks.teblocks.energy;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.block.invslot.InvSlotDischarge;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiEnergyStorage;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class TileEntityChargerBase extends TileEntityUpgradable implements IHasGui {
    public final InvSlotCharge chargeSlot;
    public final InvSlotDischarge dischargeSlot;
        
    public TileEntityChargerBase() {
        this.chargeSlot = new InvSlotCharge(this, getSourceTier());
        addChargingSlot(this.chargeSlot);
               
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.IO, getSinkTier(), false, InvSlot.InvSide.NOTSIDE);
        addDischargingSlot(this.dischargeSlot);
        
        this.energyCapacityTooltip = true;
    }
    
    @Override
    protected boolean isMultiplePacketsForTransformer() {
        return false;
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.emptySet();
    }
    
    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return allSidesExceptFacing();
    }
    
    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return getSinkSides();
    }

    public String getGuiName() {
        return GtLocale.translateTeBlock(this, "container.name");
    }
    
    @Override
    public ContainerEnergyStorage<?> getGuiContainer(EntityPlayer player) {
        return new ContainerEnergyStorage<>(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiEnergyStorage(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
