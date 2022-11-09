package mods.gregtechmod.inventory;

import mods.gregtechmod.util.DummyInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class SlotHoloItem extends Slot {
    private final Supplier<ItemStack> stack;

    public SlotHoloItem(Supplier<ItemStack> stack, int xPosition, int yPosition) {
        super(DummyInventory.INSTANCE, -1, xPosition, yPosition);
        
        this.stack = stack;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    @Override
    public ItemStack getStack() {
        return this.stack.get();
    }

    @Override
    public void putStack(ItemStack stack) {
        
    }
}
