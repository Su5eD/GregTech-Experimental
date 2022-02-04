package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBuffer;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBuffer;

public abstract class GuiElectricBuffer<T extends ContainerElectricBuffer<? extends TileEntityElectricBuffer>> extends GuiSimple<T> {

    public GuiElectricBuffer(T container) {
        super(container);

        addVerticalIconCycle(7, 62, 58, () -> container.base.outputEnergy);
        addVerticalIconCycle(25, 62, 130, () -> container.base.redstoneIfFull);
        addVerticalIconCycle(43, 62, 148, () -> container.base.invertRedstone);
    }
}
