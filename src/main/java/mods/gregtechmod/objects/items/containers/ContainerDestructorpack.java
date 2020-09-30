package mods.gregtechmod.objects.items.containers;

import ic2.core.IC2;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.ContainerHandHeldInventory;
import ic2.core.slot.SlotInvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.objects.items.ItemDestructorPack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class ContainerDestructorpack extends ContainerHandHeldInventory<ItemDestructorPack.HandHeldDestructorPack> {

    public ContainerDestructorpack(EntityPlayer player, ItemDestructorPack.HandHeldDestructorPack inventory) {
        super(inventory);
        addPlayerInventorySlots(player, 166);
        addSlotToContainer(new SlotInvSlot(new InvSlot(1), 0, 80, 17));
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slot, int button, ClickType type, EntityPlayer player) {
        if (slot < 0) return super.slotClick(slot, button, type, player);
        else if (slot < 1) {
            if (!player.inventory.getItemStack().isEmpty()) {
                if (type == ClickType.PICKUP) {
                    if (IC2.keyboard.isSneakKeyDown(player)) {
                        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                            ItemStack tStack = player.inventory.getStackInSlot(i);
                            if (!tStack.isEmpty()) {
                                if (StackUtil.checkItemEquality(tStack, player.inventory.getItemStack())) {
                                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                                }
                            }
                        }
                    }
                    player.inventory.setItemStack(ItemStack.EMPTY);
                } else if (player.inventory.getItemStack().getCount() < 2) {
                    player.inventory.setItemStack(ItemStack.EMPTY);
                } else {
                    player.inventory.getItemStack().shrink(1);
                    return player.inventory.getItemStack();
                }
            }
            return ItemStack.EMPTY;
        }
        return super.slotClick(slot, button, type, player);
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        this.getSlot(slotID).putStack(ItemStack.EMPTY);
    }

    @Override
    protected ItemStack handlePlayerSlotShiftClick(EntityPlayer player, ItemStack sourceItemStack) {
        sourceItemStack = ItemStack.EMPTY;
        return sourceItemStack;
    }
}
