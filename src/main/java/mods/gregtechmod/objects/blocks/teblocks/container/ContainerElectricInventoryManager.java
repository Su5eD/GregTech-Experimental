package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.inventory.SlotInvSlotSizeFilter;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricInventoryManager;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricInventoryManager extends ContainerGtInventory<TileEntityElectricInventoryManager> {

    public ContainerElectricInventoryManager(EntityPlayer player, TileEntityElectricInventoryManager base) {
        super(player, base);
        
        addSlotRange(base.manager.ranges.get(0), 5, true);
        addSlotRange(base.manager.ranges.get(1), 61, false);
        addSlotRange(base.manager.ranges.get(2), 80, true);
        addSlotRange(base.manager.ranges.get(3), 136, false);
        
        addSlotsToContainer(3, 1, 155, 5, base.buffer);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("manager");
    }

    private void addSlotRange(TileEntityElectricInventoryManager.SlotRange slotRange, int xOffset, boolean right) {
        int filterX = right ? 19 : -19;
        
        forEachRowCol(3, 1, xOffset, 5, 18, (index, x, y) -> {
            TileEntityElectricInventoryManager.SlotRangeSetting setting = slotRange.rangeSettings.get(index);
            
            addSlotToContainer(new SlotInvSlotSizeFilter(setting.filter, 0, x, y));
            addSlotToContainer(new SlotInteractive(x + filterX, y, click -> {
                if (click == ButtonClick.MOUSE_RIGHT) setting.switchInput();
                else if (click == ButtonClick.MOUSE_LEFT) setting.switchTargetSide();
            }));
        });
        
        addSlotToContainer(new SlotInteractive(xOffset + filterX, 60, slotRange::switchFacing));
        addSlotToContainer(new SlotInteractive(xOffset, 60, slotRange::switchOutputEnergy));
    }
}
