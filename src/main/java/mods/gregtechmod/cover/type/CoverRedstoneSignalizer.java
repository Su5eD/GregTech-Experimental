package mods.gregtechmod.cover.type;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverRedstoneSignalizer extends CoverGeneric {
    protected byte signal;

    public CoverRedstoneSignalizer(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        signal = (byte) (signal + 1 & 15);
        GtUtil.sendMessage(player, Reference.MODID+".cover.signal", signal);
        return true;
    }

    @Override
    public byte getRedstoneInput() {
        return (byte)(signal & 15);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("mode", this.signal);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.signal = nbt.getByte("mode");
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/redstone_signalizer");
    }
}
