package mods.gregtechmod.api.cover;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Lets your {@link net.minecraft.world.level.block.entity.BlockEntity} accept covers.
 */
public interface ICoverable {

    boolean removeCover(Direction side, boolean simulate);

    @Nullable
    ICover getCoverAtSide(Direction side);

    Collection<? extends ICover> getCovers();

    boolean placeCoverAtSide(ICover cover, Player player, Player side, boolean simulate);

    void updateRender();
}
