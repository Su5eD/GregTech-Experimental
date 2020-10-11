package mods.gregtechmod.cover.type;

import ic2.core.IC2;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class CoverItemMeter extends CoverGeneric {
    protected byte mode;

    public CoverItemMeter(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregtechMachine) || !(te instanceof IInventory)) return;

        IItemHandler handler = ((TileEntity)te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
        int maxCount = 0;
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            maxCount += 64;
            ItemStack stack = handler.getStackInSlot(i);
            count += (stack.getCount()*64) / stack.getMaxStackSize();
        }
        if (maxCount > 0) {
            maxCount /= 15;
            double strength = (double) count / maxCount;
            ((IGregtechMachine)te).setRedstoneOutput(side, (byte) (mode == 0 ? strength : 15-strength));
        } else {
            ((IGregtechMachine)te).setRedstoneOutput(side, (byte) (mode == 0 ? 0 : 15));
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode + 1)%2);
        if (!player.world.isRemote) return true;

        if (mode == 0) IC2.platform.messagePlayer(player, "Normal");
        else IC2.platform.messagePlayer(player, "Inverted");
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("mode", this.mode);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = nbt.getByte("mode");
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregTechMod.MODID, "blocks/covers/item_meter");
    }

    @Override
    public short getTickRate() {
        return 5;
    }

    @Override
    public void onCoverRemoval() {
        if (te instanceof IGregtechMachine) ((IGregtechMachine) te).setRedstoneOutput(side, (byte) 0);
    }

    @Override
    public boolean allowEnergyTransfer() {
        return true;
    }

    @Override
    public boolean letsRedstoneIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneOut() {
        return true;
    }

    @Override
    public boolean letsLiquidsIn() {
        return true;
    }

    @Override
    public boolean letsLiquidsOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn() {
        return true;
    }

    @Override
    public boolean letsItemsOut() {
        return true;
    }

    @Override
    public boolean overrideRedstoneOut() {
        return true;
    }
}
