package mods.gregtechmod.util;

import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Redstone;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import net.minecraft.util.EnumFacing;

public class SidedRedstone extends Redstone {

    public <T extends TileEntityBlock & ICoverable> SidedRedstone(T parent) {
        super(parent);
    }

    public int getRedstoneInput(EnumFacing side) {
        ICover cover;
        if ((cover = ((ICoverable)parent).getCoverAtSide(side)) == null) return super.getRedstoneInput();
        return Math.max(super.getRedstoneInput(), cover.getRedstoneInput());
    }
}
