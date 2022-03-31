package mods.gregtechmod.api.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public interface IScannerInfoProvider {
    @Nonnull
    List<ITextComponent> getScanInfo(EntityPlayer player, BlockPos pos, int scanLevel);
}
