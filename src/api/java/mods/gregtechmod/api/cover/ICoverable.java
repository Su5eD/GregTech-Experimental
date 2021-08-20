package mods.gregtechmod.api.cover;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Lets your {@link net.minecraft.tileentity.TileEntity TileEntity} accept covers.
 */
public interface ICoverable {

    boolean removeCover(EnumFacing side, boolean simulate);

    @Nullable
    ICover getCoverAtSide(EnumFacing side);

    Collection<? extends ICover> getCovers();

    boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate);

    void updateRender();
}
