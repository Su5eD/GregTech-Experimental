package dev.su5ed.gregtechmod.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CoverType extends ForgeRegistryEntry<CoverType> {
    private final CoverSupplier factory;

    public CoverType(CoverSupplier factory) {
        this.factory = factory;
    }
    
    public ICover create(ICoverable parent, Direction side, Item item) {
        return this.factory.create(this, parent, side, item);
    }

    @FunctionalInterface
    public interface CoverSupplier {
        /**
         * @param side   The cover's side
         * @param parent The <code>{@link BlockEntity BlockEntity}</code> being covered
         * @param item   The cover <code>{@link Item}</code>
         * @return A new cover instance
         */
        ICover create(CoverType type, ICoverable parent, Direction side, Item item);
    }
}
