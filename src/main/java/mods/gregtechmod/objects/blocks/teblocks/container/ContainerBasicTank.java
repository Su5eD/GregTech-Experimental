package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.TileEntityBlock;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import java.util.List;

public class ContainerBasicTank<T extends TileEntityBlock & IInventory> extends ContainerGtInventory<T> {
    
    public ContainerBasicTank(EntityPlayer player, T base) {
        this(player, base, 80);
    }
    
    public ContainerBasicTank(EntityPlayer player, T base, int xOffset) {
        super(player, base);
        initSlots(xOffset);
    }

    protected void initSlots(int xOffset) {
        BasicTank tank = this.base.getComponent(BasicTank.class);
        if (tank == null) throw new IllegalStateException("TileEntity " + this.base.getName() + " is missing a BasicTank component");
        
        addSlotToContainer(new SlotInvSlot(tank.inputSlot, 0, xOffset, 17));
        addSlotToContainer(new SlotInvSlot(tank.outputSlot, 0, xOffset, 53));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("tank");
    }
}
