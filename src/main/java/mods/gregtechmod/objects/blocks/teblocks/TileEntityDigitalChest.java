package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityDigitalChestBase;

public class TileEntityDigitalChest extends TileEntityDigitalChestBase {

    public TileEntityDigitalChest() {
        super(GregTechConfig.FEATURES.digitalChestMaxItemCount);
    }
}
