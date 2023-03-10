package dev.su5ed.gtexperimental.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;

public class CoverType {
    private final CoverCategory category;
    private final Predicate<BlockEntity> condition;
    private final CoverSupplier factory;

    public CoverType(CoverCategory category, Predicate<BlockEntity> condition, CoverSupplier factory) {
        this.category = category;
        this.condition = condition;
        this.factory = factory;
    }

    public CoverCategory getCategory() {
        return this.category;
    }

    public Predicate<BlockEntity> getCondition() {
        return this.condition;
    }

    public Cover create(BlockEntity parent, Direction side, Item item) {
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
        Cover create(CoverType type, BlockEntity parent, Direction side, Item item);
    }
}
