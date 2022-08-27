package dev.su5ed.gregtechmod.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.Optional;

public interface CoverHandler {
    Map<Direction, Cover<?>> getCovers();
    
    Optional<Cover<?>> getCoverAtSide(Direction side);
    
    <T> boolean placeCoverAtSide(CoverType<T> type, Direction side, Item item, boolean simulate);
    
    Optional<Cover<?>> removeCover(Direction side, boolean simulate);
}
