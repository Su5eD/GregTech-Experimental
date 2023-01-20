package dev.su5ed.gtexperimental.util.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotInventory extends Slot {
    private static final Container EMPTY_INVENTORY = new SimpleContainer(0);

    private final InventorySlot inventorySlot;
    private final int index;

    public SlotInventory(InventorySlot inventorySlot, int index, int x, int y) {
        super(EMPTY_INVENTORY, -1, x, y);

        this.inventorySlot = inventorySlot;
        this.index = index;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return !stack.isEmpty() && this.inventorySlot.canInsert(stack);
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        return this.inventorySlot.get(this.index);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        this.inventorySlot.setItem(this.index, stack);
        setChanged();
    }

    @Override
    public void initialize(ItemStack stack) {
        this.inventorySlot.setItem(this.index, stack);
        setChanged();
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {}

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 64;
    }

    @Override
    public boolean mayPickup(Player player) {
        return this.inventorySlot.canExtract(1);
    }

    @Override
    @NotNull
    public ItemStack remove(int amount) {
        return this.inventorySlot.extract(this.index, amount);
    }
}
