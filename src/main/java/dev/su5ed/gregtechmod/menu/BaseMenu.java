package dev.su5ed.gregtechmod.menu;

import dev.su5ed.gregtechmod.util.TriConsumer;
import dev.su5ed.gregtechmod.util.TriFunction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMenu extends AbstractContainerMenu {
    protected final List<Slot> hotBarSlots = new ArrayList<>();
    protected final List<Slot> playerInventorySlots = new ArrayList<>();

    protected BaseMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    protected void addPlayerInventorySlots(Inventory inventory) {
        addPlayerInventorySlots(new InvWrapper(inventory), 178, 166);
    }

    protected void addPlayerInventorySlots(IItemHandler inventory, int width, int height) {
        int leftCol = (width - 162) / 2;
        TriFunction<Integer, Integer, Integer, Slot> factory = (index, x, y) -> new SlotItemHandler(inventory, index, x, y);
        
        // Player inventory
        addSlotBox(9, leftCol, height - 82, 9, 3, 18, 18, (index, x, y) -> this.playerInventorySlots.add(addSlot(factory.apply(index, x, y))));

        // Hotbar
        addSlotRange(0, leftCol, height - 24, 9, 18, (index, x, y) -> this.hotBarSlots.add(addSlot(factory.apply(index, x, y))));
    }

    protected int addSlotRange(int index, int x, int y, int amount, int xOffset, TriConsumer<Integer, Integer, Integer> factory) {
        for (int i = 0; i < amount; i++) {
            factory.accept(index, x, y);
            x += xOffset;
            index++;
        }
        return index;
    }

    protected int addSlotBox(int x, int y, int horAmount, int verAmount, int xOffset, int yOffset, TriConsumer<Integer, Integer, Integer> factory) {
        return addSlotBox(0, x, y, horAmount, verAmount, xOffset, yOffset, factory);
    }
    
    protected int addSlotBox(int index, int x, int y, int horAmount, int verAmount, int xOffset, int yOffset, TriConsumer<Integer, Integer, Integer> factory) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(index, x, y, horAmount, xOffset, factory);
            y += yOffset;
        }
        return index;
    }
}
