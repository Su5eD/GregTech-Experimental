package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntityAESU;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAESU extends ContainerEnergyStorage<TileEntityAESU> {
    
    public ContainerAESU(EntityPlayer player, TileEntityAESU base) {
        super(player, base);
        
        addSlotToContainer(new Slot(base, -1, 107, 5));
        addSlotToContainer(new Slot(base, -1, 107, 23));
        addSlotToContainer(new Slot(base, -1, 107, 41));
        addSlotToContainer(new Slot(base, -1, 107, 59));
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        switch (slotId) {
            case 42:
                increaseOutputVoltage(clickType, 64, 256);
                break;
            case 43:
                increaseOutputVoltage(clickType, 1, 16);
                break;
            case 44:
                decreaseOutputVoltage(clickType, 1, 16);
                break;
            case 45:
                decreaseOutputVoltage(clickType, 64, 256);
                break;
        }
        return super.slotClick(slotId, dragType, clickType, player);
    }
    
    private void increaseOutputVoltage(ClickType clickType, int min, int max) {
        this.base.outputVoltage = Math.min(this.base.maxOutputVoltage, this.base.outputVoltage + (clickType == ClickType.QUICK_MOVE ? max : min));
    }
    
    private void decreaseOutputVoltage(ClickType clickType, int min, int max) {
        this.base.outputVoltage = Math.max(0, this.base.outputVoltage - (clickType == ClickType.QUICK_MOVE ? max : min));
    }
}
