package mods.gregtechmod.objects.items.containers;

import ic2.core.item.ContainerHandHeldInventory;
import mods.gregtechmod.objects.items.ItemDestructorPack.HandHeldDestructorPack;
import mods.gregtechmod.util.DummyInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerDestructorpack extends ContainerHandHeldInventory<HandHeldDestructorPack> {

    public ContainerDestructorpack(EntityPlayer player, HandHeldDestructorPack inventory) {
        super(inventory);

        addPlayerInventorySlots(player, 166);

        addSlotToContainer(new Slot(DummyInventory.INSTANCE, -1, 80, 17));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
