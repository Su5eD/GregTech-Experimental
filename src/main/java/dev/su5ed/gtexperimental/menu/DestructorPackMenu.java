package dev.su5ed.gtexperimental.menu;

import dev.su5ed.gtexperimental.object.ModMenus;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.util.DummyInventory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DestructorPackMenu extends BaseMenu {
    private final InteractionHand hand;

    public DestructorPackMenu(int containerId, InteractionHand hand, Inventory playerInventory) {
        super(ModMenus.DESTRUCTORPACK.get(), containerId);
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
