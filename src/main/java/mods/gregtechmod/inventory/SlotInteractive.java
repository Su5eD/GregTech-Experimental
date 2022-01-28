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
    private final boolean serverOnly;

    private SlotInteractive(int x, int y, boolean serverOnly, BiConsumer<ButtonClick, ItemStack> onSlotClick) {
        super(DummyInventory.INSTANCE, -1, x, y);

        this.onSlotClick = onSlotClick;
        this.serverOnly = serverOnly;
    }
    
    public static SlotInteractive serverOnly(int x, int y, Runnable onSlotClick) {
        return serverOnly(x, y, click -> onSlotClick.run());
    }
    
    public static SlotInteractive serverOnly(int x, int y, Consumer<ButtonClick> onSlotClick) {
        return serverOnly(x, y, (click, stack) -> onSlotClick.accept(click));
    }
    
    public static SlotInteractive serverOnly(int x, int y, BiConsumer<ButtonClick, ItemStack> onSlotClick) {
        return new SlotInteractive(x, y, true, onSlotClick);
    }
    
    public static SlotInteractive bothSides(int x, int y, Runnable onSlotClick) {
        return new SlotInteractive(x, y, false, (click, stack) -> onSlotClick.run());
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        if (!this.serverOnly || !player.world.isRemote) this.onSlotClick.accept(click, stack);
        return false;
    }
}
