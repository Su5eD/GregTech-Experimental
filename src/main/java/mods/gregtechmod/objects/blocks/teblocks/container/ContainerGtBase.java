package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotArmor;
import mods.gregtechmod.inventory.SlotInteractive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ContainerGtBase<T extends IInventory> extends ContainerFullInv<T> {
    
    public ContainerGtBase(EntityPlayer player, T base) {
        this(player, base, 166);
    }

    public ContainerGtBase(EntityPlayer player, T base, int height) {
        super(player, base, height);
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId >= 0 && slotId < this.inventorySlots.size()) {
            Slot slot = getSlot(slotId);
            if (slot instanceof SlotInteractive) ((SlotInteractive) slot).slotClick(dragType, clickType, player);
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }

    @Override
    public final List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        getNetworkedFields(ret);
        return ret;
    }
    
    public void getNetworkedFields(List<? super String> list) {}
    
    protected void addArmorSlots(EntityPlayer player, int x, int y) {
        addSlotToContainer(new SlotArmor(player.inventory, 39, x, y, EntityEquipmentSlot.HEAD));
        addSlotToContainer(new SlotArmor(player.inventory, 38, x, y + 18, EntityEquipmentSlot.CHEST));
        addSlotToContainer(new SlotArmor(player.inventory, 37, x, y + 36, EntityEquipmentSlot.LEGS));
        addSlotToContainer(new SlotArmor(player.inventory, 36, x, y + 54, EntityEquipmentSlot.FEET));
    }
    
    protected void addInvSlotToContainer(int rows, int cols, int xOffset, int yOffset, InvSlot invSlot) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int index = x + y * cols;
                int xPos = xOffset + x * 18;
                int yPos = yOffset + y * 18;
                
                addSlotToContainer(new SlotInvSlot(invSlot, index, xPos, yPos));
            }
        }
    }
}
