package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.inventory.SlotStackCycle;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeReactor;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import one.util.streamex.EntryStream;

public class ContainerComputerCubeReactor extends ContainerComputerCube {

    public ContainerComputerCubeReactor(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base, 156);

        ComputerCubeReactor module = (ComputerCubeReactor) base.getActiveModule();

        Slot slotItemSelection = new SlotStackCycle(module.selection, 0, 153, 28, ComputerCubeReactor.COMPONENTS);
        addSlotToContainer(slotItemSelection); // Item Selection

        // Save
        addSlotToContainer(SlotInteractive.serverOnly(156, 54, () -> {
            module.saveNuclearReactor();
            detectAndSendChanges();
        }));

        // Load
        addSlotToContainer(SlotInteractive.serverOnly(156, 70, () -> {
            module.loadNuclearReactor();
            detectAndSendChanges();
        }));

        // Start/Stop Reactor
        addSlotToContainer(SlotInteractive.serverOnly(156, 86, module::switchNuclearReactor));

        addSlotsToContainer(6, 9, 5, 5, 16, (index, x, y) -> new SlotStackCycle(module.content, index, x, y, ComputerCubeReactor.COMPONENTS, list -> {
            ItemStack stack = slotItemSelection.getStack();
            return EntryStream.of(list)
                .filterValues(s -> GtUtil.stackEquals(s, stack))
                .keys()
                .findFirst()
                .orElse(0);
        }));
    }
}
