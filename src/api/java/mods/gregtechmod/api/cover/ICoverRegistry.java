package mods.gregtechmod.api.cover;

import mods.gregtechmod.api.util.TriFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface ICoverRegistry {
    void registerCover(String name, TriFunction<ICoverable, EnumFacing, ItemStack, ICover> factory);
    
    ICover constructCover(String name, EnumFacing side, ICoverable parent, ItemStack stack);
    
    String getCoverName(ICover cover);
}
