package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.multiblock.TileEntityHatchMaintenance;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHatchMaintenance extends ContainerGtInventory<TileEntityHatchMaintenance> {

    public ContainerHatchMaintenance(EntityPlayer player, TileEntityHatchMaintenance base) {
        super(player, base);
        
        addSlotToContainer(new SlotInteractive(80, 35, (click, stack) -> {
            if (!stack.isEmpty()) this.base.onToolClick(stack, player);
        }));
    }
}
