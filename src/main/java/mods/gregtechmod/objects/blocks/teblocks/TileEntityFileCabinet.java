package mods.gregtechmod.objects.blocks.teblocks;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TileEntityFileCabinet extends TileEntityShelf {

    @Override
    public boolean accepts(@Nullable Type type, ItemStack stack) {
        return true;
    }
}
