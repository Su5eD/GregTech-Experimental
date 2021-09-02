package mods.gregtechmod.objects.blocks.teblocks.energy;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.block.invslot.InvSlotDischarge;
import mods.gregtechmod.gui.GuiEnergyStorage;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public abstract class TileEntityChargerBase extends TileEntityEnergy implements IHasGui {

    public final InvSlotCharge chargeSlot;
    public final InvSlotDischarge dischargeSlot;
        
    public TileEntityChargerBase(String descriptionKey) {
        super(descriptionKey);
        
        this.chargeSlot = new InvSlotCharge(this, 1);
        this.energy.addChargingSlot(this.chargeSlot);
               
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.IO, 1, false, InvSlot.InvSide.NOTSIDE);
        this.energy.addDischargingSlot(this.dischargeSlot);
    }

    @Override
    public ContainerEnergyStorage getGuiContainer(EntityPlayer player) {
        return new ContainerEnergyStorage(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiEnergyStorage(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    }
}
