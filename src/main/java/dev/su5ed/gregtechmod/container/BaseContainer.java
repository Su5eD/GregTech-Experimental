package dev.su5ed.gregtechmod.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public abstract class BaseContainer extends AbstractContainerMenu {

    protected BaseContainer(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }
    
    protected void addPlayerInventorySlots(Inventory inventory) {
        addPlayerInventorySlots(new InvWrapper(inventory), 178, 166);
    }

    protected void addPlayerInventorySlots(IItemHandler inventory, int width, int height) {
        int leftCol = (width - 162) / 2;
        
        // Player inventory
        addSlotBox(inventory, 9, leftCol, height - 82, 9, 18, 3, 18);

        // Hotbar
        addSlotRange(inventory, 0, leftCol, height - 24, 9, 18);
    }

    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
}
