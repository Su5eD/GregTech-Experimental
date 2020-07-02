package mods.gregtechmod.common.objects.blocks.machines.tileentity;

import mods.gregtechmod.common.core.ConfigLoader;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.base.TileEntityDigitalChestBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class TileEntityDigitalChest extends TileEntityDigitalChestBase {

    public TileEntityDigitalChest() {
        super(ConfigLoader.digitalChestMaxItemCount, false);
    }
}
