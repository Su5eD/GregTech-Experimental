package dev.su5ed.gregtechmod.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ICoverProvider extends IForgeRegistryEntry<ICoverProvider> {
    
    /**
     * @param side   The cover's side
     * @param parent The <code>{@link net.minecraft.world.level.block.entity.BlockEntity BlockEntity}</code> being covered
     * @param stack  The cover <code>{@link ItemStack}</code>
     * @return A new cover instance
     */
    ICover constructCover(Direction side, ICoverable parent, ItemStack stack);
}
