package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;

public class GtSlotLargeItemStack extends InvSlot {
    
    public GtSlotLargeItemStack(IInventorySlotHolder<?> base, String name, Access access) {
        super(base, name, access, 1);
    }

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        if (nbt.hasKey("content")) {
            NBTTagCompound contentTag = nbt.getCompoundTag("content");
            int count = nbt.getInteger("contentCount");
            
            ItemStack content = new ItemStack(contentTag);
            content.setCount(count);
            put(content);
        }
    }

    @Override
    public void writeToNbt(NBTTagCompound nbt) {
        ItemStack stack = get();
        if (!stack.isEmpty()) {
            NBTTagCompound tag = new NBTTagCompound();
            ItemStack content = ItemHandlerHelper.copyStackWithSize(stack, 1);
            nbt.setTag("content", content.writeToNBT(tag));
            nbt.setInteger("contentCount", stack.getCount());
        }
    }
}
