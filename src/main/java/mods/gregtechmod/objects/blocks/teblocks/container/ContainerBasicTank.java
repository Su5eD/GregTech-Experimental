package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import java.util.List;

public abstract class ContainerBasicTank<T extends IInventory> extends ContainerGtBase<T> {

    public ContainerBasicTank(EntityPlayer player, T base) {
        super(player, base, 166);
        initSlots();
    }
    
    protected void initSlots() {
        addSlotToContainer(new SlotInvSlot(getInputSlot(), 0, 80, 17));
        addSlotToContainer(new SlotInvSlot(getOutputSlot(), 0, 80, 53));
    }
    
    protected abstract InvSlot getInputSlot();
    
    protected abstract InvSlot getOutputSlot();

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("tank");
    }
}
