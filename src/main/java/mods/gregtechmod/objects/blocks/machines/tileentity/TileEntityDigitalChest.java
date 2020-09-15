package mods.gregtechmod.objects.blocks.machines.tileentity;

import mods.gregtechmod.core.ConfigLoader;
import mods.gregtechmod.objects.blocks.machines.tileentity.base.TileEntityDigitalChestBase;

public class TileEntityDigitalChest extends TileEntityDigitalChestBase {

    public TileEntityDigitalChest() {
        super(ConfigLoader.digitalChestMaxItemCount, false);
    }
}
