package dev.su5ed.gregtechmod.api.cover;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Optional;

/**
 * Lets your {@link net.minecraft.world.level.block.entity.BlockEntity BlockEntity} accept covers.
 */
public interface Coverable {
    Level getLevel();
        
    BlockPos getBlockPos();

    Collection<? extends Cover> getCovers();

    Optional<Cover> getCoverAtSide(Direction side);

    boolean placeCoverAtSide(Cover cover, Player player, Direction side, boolean simulate);

    boolean removeCover(Direction side, boolean simulate);

    void updateRender();
    
    void updateRenderClient();
}
