package mods.gregtechmod.inventory;

import mods.gregtechmod.util.ButtonClick;
import mods.gregtechmod.util.DummyInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlotInteractive extends Slot implements ISlotInteractive {
    private final BiConsumer<ButtonClick, ItemStack> onSlotClick;
    
    public SlotInteractive(int x, int y, Runnable onSlotClick) {
        this(x, y, (click, stack) -> onSlotClick.run());
    }
    
    public SlotInteractive(int x, int y, Consumer<ButtonClick> onSlotClick) {
        this(x, y, (click, stack) -> onSlotClick.accept(click));
    }

    public SlotInteractive(int x, int y, BiConsumer<ButtonClick, ItemStack> onSlotClick) {
        super(DummyInventory.INSTANCE, -1, x, y);
        
        this.onSlotClick = onSlotClick;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        this.onSlotClick.accept(click, stack);
        return false;
    }
}
