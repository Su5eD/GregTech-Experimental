package mods.gregtechmod.common.cover;

import net.minecraft.util.EnumFacing;

import java.util.Collection;

public interface ICoverable {
    boolean removeCover(EnumFacing side, boolean simulate);
    ICover getCoverAtSide(EnumFacing side);
    Collection<? extends ICover> getCovers();
    boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate);
    void markForRenderUpdate();
}
