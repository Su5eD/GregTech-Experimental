package mods.gregtechmod.common.objects.blocks.machines.tileentity;

import mods.gregtechmod.common.core.ConfigLoader;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.base.TileEntityDigitalChestBase;

public class TileEntityDigitalChest extends TileEntityDigitalChestBase {

    public TileEntityDigitalChest() {
        super(ConfigLoader.digitalChestMaxItemCount, false);
    }
}
