package dev.su5ed.gregtechmod.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ICoverProvider extends IForgeRegistryEntry<ICoverProvider> {
    
    /**
     * @param side   The cover's side
     * @param parent The <code>{@link BlockEntity BlockEntity}</code> being covered
     * @param item  The cover <code>{@link Item}</code>
     * @return A new cover instance
     */
    ICover constructCover(Direction side, ICoverable parent, Item item);
}
