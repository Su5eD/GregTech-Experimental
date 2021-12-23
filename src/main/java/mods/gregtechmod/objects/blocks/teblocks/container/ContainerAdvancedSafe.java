package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.util.StackUtil;
import mods.gregtechmod.inventory.SlotInvSlotHolo;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityAdvancedSafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class ContainerAdvancedSafe extends ContainerGtBase<TileEntityAdvancedSafe> {

    public ContainerAdvancedSafe(EntityPlayer player, TileEntityAdvancedSafe base) {
        super(player, base);
        
        addInvSlotToContainer(3, 9, 8, 23, base.content);
        
        addSlotToContainer(new SlotInvSlotHolo(base.filter, 0, 80, 5));
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId == 63 && clickType == ClickType.PICKUP) {
            ItemStack stack = player.inventory.getItemStack();
            this.base.filter.put(!stack.isEmpty() ? StackUtil.copyWithSize(stack, 1) : ItemStack.EMPTY);
            return stack;
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }
}
