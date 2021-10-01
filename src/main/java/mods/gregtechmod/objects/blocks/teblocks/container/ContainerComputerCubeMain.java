package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.IC2;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerComputerCubeMain extends ContainerFullInv<TileEntityComputerCube> {
    private final int switchSlotId;
    
    public ContainerComputerCubeMain(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base, 166);
        
        Slot switchSlot = new Slot(base, -1, 156, 4);
        addSlotToContainer(switchSlot);
        this.switchSlotId = switchSlot.slotNumber;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId == this.switchSlotId) {
            base.switchModule();
            IC2.platform.launchGui(player, base);
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }
}
