package mods.gregtechmod.api.cover;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ICoverProvider extends IForgeRegistryEntry<ICoverProvider> {
    
    /**
     * @param side   The cover's side
     * @param parent The <code>{@link net.minecraft.tileentity.TileEntity TileEntity}</code> being covered
     * @param stack  The cover <code>{@link ItemStack}</code>
     * @return A new cover instance
     */
    ICover constructCover(EnumFacing side, ICoverable parent, ItemStack stack);
}
