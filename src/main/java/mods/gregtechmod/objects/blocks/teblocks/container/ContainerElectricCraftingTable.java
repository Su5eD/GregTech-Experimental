package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotCraftingGrid;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricCraftingTable;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricCraftingTable extends ContainerGtInventory<TileEntityElectricCraftingTable> {

    public ContainerElectricCraftingTable(EntityPlayer player, TileEntityElectricCraftingTable base) {
        super(player, base);
        
        addSlotsToContainer(3, 3, 8, 5, base.input);
        addSlotsToContainer(3, 3, 64, 6, 17, (index, x, y) -> new SlotCraftingGrid(base.craftingGrid, index, x, y));
        addSlotToContainer(new SlotInvSlot(base.crafting, 0, 152, 5));
        addSlotToContainer(new SlotInvSlot(base.output, 0, 152, 41));
        addSlotsToContainer(1, 9, 8, 60, base.buffer);
        
        addSlotToContainer(SlotInteractive.serverOnly(121, 5, base::nextThroughPutMode));
        addSlotToContainer(SlotInteractive.serverOnly(121, 41, click -> {
            if (click == ButtonClick.MOUSE_LEFT) base.nextCraftingMode();
            else if (click == ButtonClick.MOUSE_RIGHT) base.previousCraftingMode();
        }));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("throughPutMode");
        list.add("craftingMode");
        list.add("tank");
    }
}
