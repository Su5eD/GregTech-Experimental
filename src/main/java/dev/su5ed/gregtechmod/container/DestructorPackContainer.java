package dev.su5ed.gregtechmod.container;

import dev.su5ed.gregtechmod.object.ModContainers;
import dev.su5ed.gregtechmod.object.Tool;
import dev.su5ed.gregtechmod.util.DummyInventory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DestructorPackContainer extends BaseContainer {
    private final InteractionHand hand;

    public DestructorPackContainer(int containerId, InteractionHand hand, Inventory playerInventory) {
        super(ModContainers.DESTRUCTORPACK.get(), containerId);
        this.hand = hand;

        addPlayerInventorySlots(playerInventory);
        addSlot(new Slot(DummyInventory.INSTANCE, -1, 80, 35));
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(this.hand).is(Tool.DESTRUCTORPACK.getItem());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        this.slots.get(index).set(ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }
}
