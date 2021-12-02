package mods.gregtechmod.inventory;

import mods.gregtechmod.api.util.TriConsumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotInteractive extends Slot {
    private final TriConsumer<Integer, ClickType, EntityPlayer> onSlotClick;

    public SlotInteractive(IInventory inv, int index, int x, int y, TriConsumer<Integer, ClickType, EntityPlayer> onSlotClick) {
        super(inv, index, x, y);
        
        this.onSlotClick = onSlotClick;
    }
    
    public void slotClick(int dragType, ClickType clickType, EntityPlayer player) {
        this.onSlotClick.accept(dragType, clickType, player);
    }
}
