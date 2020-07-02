package mods.gregtechmod.common.cover.types;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.cover.ICoverable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverNormal extends CoverGeneric {
    private byte mode;

    public CoverNormal(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregtechMod.MODID, "blocks/covers/"+(mode == 0 ? "normal" : "noredstone"));
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode+1)%2);
        te.markForRenderUpdate();
        return true;
    }

    @Override
    public boolean letsRedstoneIn() {
        return mode == 0;
    }

    @Override
    public boolean letsRedstoneOut() {
        return mode == 0;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return mode == 1;
    }

    @Override
    public boolean letsLiquidsIn() {
        return mode == 1;
    }

    @Override
    public boolean letsLiquidsOut() {
        return mode == 1;
    }

    @Override
    public boolean letsItemsIn() {
        return mode == 1;
    }

    @Override
    public boolean letsItemsOut() {
        return mode == 1;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("mode", mode);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = nbt.getByte("mode");
    }
}
