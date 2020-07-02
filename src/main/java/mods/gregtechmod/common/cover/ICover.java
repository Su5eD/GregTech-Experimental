package mods.gregtechmod.common.cover;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public interface ICover {
    //TODO: move to api

    void doCoverThings();

    boolean onCoverRightClick(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ);

    boolean onScrewdriverClick(EntityPlayer player);

    boolean allowEnergyTransfer();

    boolean letsRedstoneIn();

    boolean letsRedstoneOut();

    boolean letsLiquidsIn();

    boolean letsLiquidsOut();

    boolean letsItemsIn();

    boolean letsItemsOut();

    boolean opensGui(EnumFacing side);

    boolean acceptsRedstone();

    boolean overrideRedstoneOut();

    ResourceLocation getIcon();

    EnumFacing getSide();

    ItemStack getItem();

    NBTTagCompound writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    short getTickRate();

    void onCoverRemoved();
}
