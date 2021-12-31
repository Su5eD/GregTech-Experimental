package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntityAESU;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAESU extends ContainerEnergyStorage<TileEntityAESU> {
    
    public ContainerAESU(EntityPlayer player, TileEntityAESU base) {
        super(player, base);
        
        addSlotToContainer(new SlotInteractive(107, 5, click -> increaseOutputVoltage(click, 64, 256)));
        addSlotToContainer(new SlotInteractive(107, 23, click -> increaseOutputVoltage(click, 1, 16)));
        addSlotToContainer(new SlotInteractive(107, 41, click -> decreaseOutputVoltage(click, 1, 16)));
        addSlotToContainer(new SlotInteractive(107, 59, click -> decreaseOutputVoltage(click, 64, 256)));
    }
    
    private void increaseOutputVoltage(ButtonClick click, int min, int max) {
        this.base.outputVoltage = Math.min(this.base.maxOutputVoltage, this.base.outputVoltage + (click == ButtonClick.SHIFT_MOVE ? max : min));
    }
    
    private void decreaseOutputVoltage(ButtonClick click, int min, int max) {
        this.base.outputVoltage = Math.max(0, this.base.outputVoltage - (click == ButtonClick.SHIFT_MOVE ? max : min));
    }
}
