package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBuffer;

public abstract class GuiElectricBuffer<T extends ContainerElectricBuffer<?>> extends GuiSimple<T> {

    public GuiElectricBuffer(T container) {
        super(container);
        
        addVerticalCycleButton(0, 58, 0, 76, 18, 7, 62, () -> container.base.outputEnergy ? 1 : 0);
        addVerticalCycleButton(2, 130, 0, 148, 18, 25, 62, () -> container.base.redstoneIfFull ? 1 : 0);
        addVerticalCycleButton(4, 148, 0, 166, 18, 43, 62, () -> container.base.invertRedstone ? 1 : 0);
    }
}
