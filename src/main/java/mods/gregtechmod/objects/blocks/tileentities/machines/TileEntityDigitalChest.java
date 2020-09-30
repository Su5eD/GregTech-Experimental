package mods.gregtechmod.objects.blocks.tileentities.machines;

import mods.gregtechmod.core.ConfigLoader;
import mods.gregtechmod.objects.blocks.tileentities.machines.base.TileEntityDigitalChestBase;

public class TileEntityDigitalChest extends TileEntityDigitalChestBase {

    public TileEntityDigitalChest() {
        super(ConfigLoader.digitalChestMaxItemCount, false);
    }
}
