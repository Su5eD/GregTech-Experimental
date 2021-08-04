package mods.gregtechmod.api.cover;

import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Lets your {@link net.minecraft.tileentity.TileEntity TileEntity} accept covers. Keep in mind that you also need a cover renderer and something to store the covers in nbt
 */
public interface ICoverable {

    boolean removeCover(EnumFacing side, boolean simulate);

    @Nullable
    ICover getCoverAtSide(EnumFacing side);

    Collection<? extends ICover> getCovers();

    boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate);

    void updateRender();
}
