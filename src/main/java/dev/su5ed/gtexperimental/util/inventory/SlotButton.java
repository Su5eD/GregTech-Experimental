package dev.su5ed.gtexperimental.util.inventory;

import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlotButton extends Slot implements InteractiveSlot {
    private final BiConsumer<ButtonClick, ItemStack> onSlotClick;
    private final boolean serverOnly;

    private SlotButton(int x, int y, boolean serverOnly, BiConsumer<ButtonClick, ItemStack> onSlotClick) {
        super(GtUtil.EMPTY_INVENTORY, -1, x, y);

        this.onSlotClick = onSlotClick;
        this.serverOnly = serverOnly;
    }

    public static SlotButton serverOnly(int x, int y, Runnable onSlotClick) {
        return serverOnly(x, y, click -> onSlotClick.run());
    }

    public static SlotButton serverOnly(int x, int y, Consumer<ButtonClick> onSlotClick) {
        return serverOnly(x, y, (click, stack) -> onSlotClick.accept(click));
    }

    public static SlotButton serverOnly(int x, int y, BiConsumer<ButtonClick, ItemStack> onSlotClick) {
        return new SlotButton(x, y, true, onSlotClick);
    }

    public static SlotButton bothSides(int x, int y, Runnable onSlotClick) {
        return new SlotButton(x, y, false, (click, stack) -> onSlotClick.run());
    }

    @Override
    public void slotClick(ButtonClick click, Player player, ItemStack stack) {
        if (!this.serverOnly || !player.level.isClientSide) {
            this.onSlotClick.accept(click, stack);
        }
    }

    @Override
    public void slotScroll(Player player, ScrollDirection direction, boolean shift) {}

    @Override
    public void initialize(ItemStack stack) {}

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean allowModification(Player player) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack safeTake(int count, int decrement, Player player) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack safeInsert(ItemStack stack) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack safeInsert(ItemStack stack, int increment) {
        return ItemStack.EMPTY;
    }
}
