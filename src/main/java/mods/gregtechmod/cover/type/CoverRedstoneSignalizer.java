package mods.gregtechmod.cover.type;

import ic2.core.IC2;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverRedstoneSignalizer extends CoverGeneric {
    protected byte mode;

    public CoverRedstoneSignalizer(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode + 1) & 15);

        if (!player.world.isRemote) IC2.platform.messagePlayer(player, "Signal = "+ mode);

        return true;
    }

    @Override
    public byte getRedstoneInput() {
        return (byte)(mode&15);
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
        return new ResourceLocation(GregTechMod.MODID, "blocks/covers/redstone_signalizer");
    }
}
