package dev.su5ed.gregtechmod.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IScannerInfoProvider {
    @NotNull
    List<String> getScanInfo(Player player, BlockPos pos, int scanLevel);
}
