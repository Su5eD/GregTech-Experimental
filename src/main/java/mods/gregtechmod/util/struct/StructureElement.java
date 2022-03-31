package mods.gregtechmod.util.struct;

import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class StructureElement {
    public final Predicate<BlockPos> predicate;
    public final int minCount;
    public final int maxCount;

    public StructureElement(Predicate<BlockPos> predicate) {
        this(predicate, 0, 0);
    }

    public StructureElement(Predicate<BlockPos> predicate, int minCount, int maxCount) {
        if (minCount < 0 || maxCount < 0) throw new IllegalArgumentException("Count must not be negative");
        else if (maxCount > 0 && maxCount < minCount) throw new IllegalArgumentException("minCount must be lower than maxCount");

        this.predicate = predicate;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }
}
