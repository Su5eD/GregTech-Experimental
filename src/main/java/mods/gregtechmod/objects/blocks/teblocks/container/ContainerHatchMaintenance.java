package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import mods.gregtechmod.objects.blocks.teblocks.multiblock.TileEntityHatchMaintenance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHatchMaintenance extends ContainerFullInv<TileEntityHatchMaintenance> {
    private final Slot slot = new Slot(base, 0, 80, 35);

    public ContainerHatchMaintenance(EntityPlayer player, TileEntityHatchMaintenance base) {
        super(player, base, 166);
        
        addSlotToContainer(slot);
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId == slot.slotNumber) {
            ItemStack stack = player.inventory.getItemStack();
            if (!stack.isEmpty()) {
                this.base.onToolClick(stack, player);
            }
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }
}
