package mods.gregtechmod.gui;

import mods.gregtechmod.gui.element.GeneratorFluidSlot;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;

public abstract class GuiFluidGenerator extends GuiBasicTank<ContainerBasicTank<TileEntityFluidGenerator>> {

    public GuiFluidGenerator(ContainerBasicTank<TileEntityFluidGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }

    @Override
    protected void addFluidSlot() {
        addElement(new GeneratorFluidSlot(this, 58, 41, this.fluidTank));
    }
}
