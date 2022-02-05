package dev.su5ed.gregtechmod.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.List;

public interface IScannerInfoProvider {
    @Nonnull
    List<String> getScanInfo(Player player, BlockPos pos, int scanLevel);
}
