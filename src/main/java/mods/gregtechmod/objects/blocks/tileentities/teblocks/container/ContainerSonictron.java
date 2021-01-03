package mods.gregtechmod.objects.blocks.tileentities.teblocks.container;

import ic2.core.ContainerBase;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSonictron extends ContainerBase<IInventory> {

    public ContainerSonictron(IInventory base) {
        super(base);

        for (int j = 0; j < 8; j++)
            for (int i = 0; i < 8; i++)
                addSlotToContainer(new Slot(base, i+j*8, 24+16*i, 19+16*j));
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId < 0) return super.slotClick(slotId, dragType, clickType, player);
        Slot slot = this.inventorySlots.get(slotId);
        ItemStack content = slot.getStack();

        if (clickType == ClickType.QUICK_MOVE)
            slot.putStack(ItemStack.EMPTY);
        else if (clickType == ClickType.PICKUP && dragType < 1) {
            if (content.isEmpty())
                slot.putStack(StackUtil.copy(new ItemStack(GregTechAPI.sonictronSounds.get(0).item)));
            else {
                for (int i = 1; i < GregTechAPI.sonictronSounds.size(); i++) {
                    if (StackUtil.checkItemEquality(content, GregTechAPI.sonictronSounds.get(i -1).item)) {
                        slot.putStack(StackUtil.copy(new ItemStack(GregTechAPI.sonictronSounds.get(i).item)));
                        return ItemStack.EMPTY;
                    }
                }
                slot.putStack(ItemStack.EMPTY);
            }
        }
        else {
            if (!content.isEmpty()) {
                for (int i = 0; i < GregTechAPI.sonictronSounds.size(); i++) {
                    if (StackUtil.checkItemEquality(content, GregTechAPI.sonictronSounds.get(i).item)) {
                        content.grow(1);
                        content.setCount(content.getCount()%(GregTechAPI.sonictronSounds.get(i).count+1));
                        if (content.getCount() == 0) content.grow(1);
                        break;
                    }
                }
            }
        }

        return ItemStack.EMPTY;
    }
}
