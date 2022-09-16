package dev.su5ed.gregtechmod.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CoverType<T> {
    private final CoverCategory category;
    private final Class<T> coverableClass;
    private final CoverSupplier<T> factory;

    public CoverType(CoverCategory category, Class<T> coverableClass, CoverSupplier<T> factory) {
        this.category = category;
        this.coverableClass = coverableClass;
        this.factory = factory;
    }

    public CoverCategory getCategory() {
        return this.category;
    }

    public Class<T> getCoverableClass() {
        return this.coverableClass;
    }

    public Cover<T> create(T parent, Direction side, Item item) {
        return this.factory.create(this, parent, side, item);
    }

    @FunctionalInterface
    public interface CoverSupplier<T> {
        /**
         * @param side   The cover's side
         * @param parent The <code>{@link BlockEntity BlockEntity}</code> being covered
         * @param item   The cover <code>{@link Item}</code>
         * @return A new cover instance
         */
        Cover<T> create(CoverType<T> type, T parent, Direction side, Item item);
    }
}
