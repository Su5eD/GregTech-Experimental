package mods.gregtechmod.cover;

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

    public CoverItemMeter(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IInventory)) return;

        super.doCoverThings();
    }

    @Override
    protected Pair<Integer, Integer> getStorageAndCapacity() {
        IItemHandler handler = ((TileEntity)te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
        int maxCount = 0;
        int count = 0;
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {
                maxCount += 64;
                ItemStack stack = handler.getStackInSlot(i);
                count += stack.getCount() * 64 / stack.getMaxStackSize();
            }
        }

        return Pair.of(count, maxCount);
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
