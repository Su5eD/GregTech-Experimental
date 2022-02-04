package mods.gregtechmod.api.cover;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Used to create covers, providing <code>{@link ICoverable}</code> machines all the information they need
 */
public interface ICover {

    ResourceLocation getName();

    /**
     * Ticked every n tick(s), depending on the cover's {@link ICover#getTickRate() tick rate}
     */
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

    /**
     * Declares whether or not can a player access the parent's GUI when activating <b>any of the sides</b>
     *
     * @param side the activated side
     * @return a <b>condition</b> under which the gui can be accessed. <b>NEVER</b> return <code>true</code> or <code>false</code> as it will break other covers (especially the Screen). For example, <code>side != this.side</code> or <code>side == this.side</code>
     */
    boolean opensGui(EnumFacing side);

    boolean acceptsRedstone();

    boolean overrideRedstoneOut();

    int getRedstoneInput();

    /**
     * @return The location of the cover's texture
     */
    ResourceLocation getIcon();

    /**
     * @return The cover's side
     */
    EnumFacing getSide();

    /**
     * @return The cover's associated item
     */
    ItemStack getItem();

    @Nonnull
    List<String> getDescription();

    CoverType getType();

    NBTTagCompound writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    int getTickRate();

    /**
     * Called just before the cover is removed from a machine
     */
    void onCoverRemove();
}
