package mods.gregtechmod.inventory;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class SlotInteractive extends Slot implements ISlotInteractive {
    private final Consumer<ClickType> onSlotClick;
    
    public SlotInteractive(IInventory inv, int x, int y, Consumer<ClickType> onSlotClick) {
        this(inv, -1, x, y, onSlotClick);
    }

    public SlotInteractive(IInventory inv, int index, int x, int y, Consumer<ClickType> onSlotClick) {
        super(inv, index, x, y);
        
        this.onSlotClick = onSlotClick;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean slotClick(ClickType clickType, ItemStack stack) {
        this.onSlotClick.accept(clickType);
        return false;
    }
}
