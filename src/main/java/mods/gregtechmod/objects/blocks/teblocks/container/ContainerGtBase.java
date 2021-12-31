package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerBase;
import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.inventory.ISlotInteractive;
import mods.gregtechmod.inventory.SlotArmor;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ContainerGtBase<T extends IInventory> extends ContainerBase<T> {

    public ContainerGtBase(T base) {
        super(base);
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId >= 0 && slotId < this.inventorySlots.size()) {
            Slot slot = getSlot(slotId);
            if (slot instanceof ISlotInteractive) {
                ButtonClick click = ButtonClick.fromClickType(clickType, dragType);
                
                if (click != null) {
                    ItemStack stack = player.inventory.getItemStack();
                    boolean result = ((ISlotInteractive) slot).slotClick(click, player, stack);
                    
                    this.base.markDirty();
                    if (result) return stack;
                }
            }
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
    
    protected void addSlotsToContainer(int rows, int cols, int xOffset, int yOffset, InvSlot invSlot) {
        addSlotsToContainer(rows, cols, xOffset, yOffset, 18, (index, x, y) -> new SlotInvSlot(invSlot, index, x, y));
    }
    
    protected void addSlotsToContainer(int rows, int cols, int xOffset, int yOffset, int slotOffset, TriFunction<Integer, Integer, Integer, Slot> slotFactory) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int index = x + y * cols;
                int xPos = xOffset + x * slotOffset;
                int yPos = yOffset + y * slotOffset;
                
                addSlotToContainer(slotFactory.apply(index, xPos, yPos));
            }
        }
    }
}
