package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.IC2;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerComputerCube extends ContainerFullInv<TileEntityComputerCube> {
    
    public ContainerComputerCube(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base, 166);
        
        addSlotToContainer(new Slot(base, 0, 156, 4));
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId == 36) {
            base.switchModule();
            IC2.platform.launchGui(player, base);
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }
}
