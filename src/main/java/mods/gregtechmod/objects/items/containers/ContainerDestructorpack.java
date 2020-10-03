package mods.gregtechmod.objects.items.containers;

import ic2.core.item.ContainerHandHeldInventory;
import mods.gregtechmod.objects.items.ItemDestructorPack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDestructorpack extends ContainerHandHeldInventory<ItemDestructorPack.HandHeldDestructorPack> {

    public ContainerDestructorpack(EntityPlayer player, ItemDestructorPack.HandHeldDestructorPack inventory) {
        super(inventory);
        addPlayerInventorySlots(player, 166);
        addSlotToContainer(new Slot(player.inventory, this.inventorySlots.size(), 80, 17));
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slot, int button, ClickType type, EntityPlayer player) {
        if (slot == 36) {
            player.inventory.setItemStack(ItemStack.EMPTY);
        }
        return super.slotClick(slot, button, type, player);
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        if (slotID == 36) super.putStackInSlot(slotID, ItemStack.EMPTY);
        else super.putStackInSlot(slotID, stack);
    }

    @Override
    protected ItemStack handlePlayerSlotShiftClick(EntityPlayer player, ItemStack sourceItemStack) {
        sourceItemStack = ItemStack.EMPTY;
        return sourceItemStack;
    }
}
