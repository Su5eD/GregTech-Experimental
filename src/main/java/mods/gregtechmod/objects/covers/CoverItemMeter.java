package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

public class CoverItemMeter extends CoverMeter {

    public CoverItemMeter(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IInventory)) return;

        super.doCoverThings();
    }

    @Override
    protected Pair<Integer, Integer> getItemStorageAndCapacity() {
        IItemHandler handler = ((TileEntity)te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
        int capacity = 0;
        int storage = 0;
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {;
                ItemStack stack = handler.getStackInSlot(i);
                int maxStackSize = stack.getMaxStackSize();
                capacity += maxStackSize;
                storage += stack.getCount() * 64 / maxStackSize;
            }
        }

        return Pair.of(storage, capacity);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/item_meter");
    }

    @Override
    public boolean letsRedstoneIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneOut() {
        return true;
    }
}
