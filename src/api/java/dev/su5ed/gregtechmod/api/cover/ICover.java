package dev.su5ed.gregtechmod.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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

    boolean onCoverRightClick(Player player, InteractionHand hand, Direction side, float hitX, float hitY, float hitZ);

    boolean onScrewdriverClick(Player player);

    boolean allowEnergyTransfer();

    boolean letsRedstoneIn();

    boolean letsRedstoneOut();

    boolean letsLiquidsIn();

    boolean letsLiquidsOut();

    boolean letsItemsIn();

    boolean letsItemsOut();

    /**
     * Declares whether or not can a player access the parent's GUI when activating <b>any of the sides</b>
     * @param side the activated side
     * @return a <b>condition</b> under which the gui can be accessed. <b>NEVER</b> return <code>true</code> or <code>false</code> as it will break other covers (especially the Screen). For example, <code>side != this.side</code> or <code>side == this.side</code>
     */
    boolean opensGui(Direction side);

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
    Direction getSide();

    /**
     * @return The cover's associated item
     */
    ItemStack getItem();
    
    CoverType getType();

    CompoundTag save(CompoundTag tag);

    void load(CompoundTag tag);

    int getTickRate();

    /**
     * Called just before the cover is removed from a machine
     */
    void onCoverRemove();
}
