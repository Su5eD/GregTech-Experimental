package mods.gregtechmod.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotArmor extends Slot {
    private final EntityEquipmentSlot armorType;

    public SlotArmor(IInventory inventoryIn, int index, int xPosition, int yPosition, EntityEquipmentSlot armorType) {
        super(inventoryIn, index, xPosition, yPosition);
        this.armorType = armorType;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().isValidArmor(stack, this.armorType, ((InventoryPlayer)this.inventory).player);
    }
}
