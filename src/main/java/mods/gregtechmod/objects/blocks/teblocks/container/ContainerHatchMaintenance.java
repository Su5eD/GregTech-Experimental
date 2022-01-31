package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.multiblock.TileEntityHatchMaintenance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ContainerHatchMaintenance extends ContainerGtInventory<TileEntityHatchMaintenance> {

    public ContainerHatchMaintenance(EntityPlayer player, TileEntityHatchMaintenance base) {
        super(player, base);
        
        addSlotToContainer(SlotInteractive.serverOnly(80, 35, (click, stack) -> {
            if (!stack.isEmpty()) {
                this.base.onToolClick(stack, player);
                ((EntityPlayerMP) player).updateHeldItem();
            }
        }));
    }
}
