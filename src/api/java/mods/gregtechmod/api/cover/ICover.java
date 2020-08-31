package mods.gregtechmod.api.cover;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public interface ICover {
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

    byte getRedstoneInput();

    ResourceLocation getIcon();

    EnumFacing getSide();

    ItemStack getItem();

    @Nonnull
    List<String> getAdditionalInformation();

    NBTTagCompound writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    short getTickRate();

    void onCoverRemoval();
}
