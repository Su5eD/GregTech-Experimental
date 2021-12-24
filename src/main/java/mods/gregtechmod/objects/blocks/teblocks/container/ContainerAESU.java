package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntityAESU;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;

public class ContainerAESU extends ContainerEnergyStorage<TileEntityAESU> {
    
    public ContainerAESU(EntityPlayer player, TileEntityAESU base) {
        super(player, base);
        
        addSlotToContainer(new SlotInteractive(base, 107, 5, clickType -> increaseOutputVoltage(clickType, 64, 256)));
        addSlotToContainer(new SlotInteractive(base, 107, 23, clickType -> increaseOutputVoltage(clickType, 1, 16)));
        addSlotToContainer(new SlotInteractive(base, 107, 41, clickType -> decreaseOutputVoltage(clickType, 1, 16)));
        addSlotToContainer(new SlotInteractive(base, 107, 59, clickType -> decreaseOutputVoltage(clickType, 64, 256)));
    }
    
    private void increaseOutputVoltage(ClickType clickType, int min, int max) {
        this.base.outputVoltage = Math.min(this.base.maxOutputVoltage, this.base.outputVoltage + (clickType == ClickType.QUICK_MOVE ? max : min));
    }
    
    private void decreaseOutputVoltage(ClickType clickType, int min, int max) {
        this.base.outputVoltage = Math.max(0, this.base.outputVoltage - (clickType == ClickType.QUICK_MOVE ? max : min));
    }
}
