package mods.gregtechmod.objects.blocks.teblocks.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public abstract class ContainerGtInventory<T extends IInventory> extends ContainerGtBase<T> {

    public ContainerGtInventory(EntityPlayer player, T base) {
        super(base);
        
        addPlayerInventorySlots(player, 166);
    }
}
