package mods.gregtechmod.api.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public interface IScannerInfoProvider {
    @Nonnull
    List<String> getScanInfo(EntityPlayer player, BlockPos pos, int scanLevel);
}
