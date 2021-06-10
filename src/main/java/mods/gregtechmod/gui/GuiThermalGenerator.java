package mods.gregtechmod.gui;

import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.inventory.ThermalGeneratorFluidSlot;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;
import mods.gregtechmod.objects.blocks.teblocks.generator.TileEntityThermalGenerator;

public class GuiThermalGenerator extends GuiBasicTank<ContainerBasicTank<TileEntityThermalGenerator>> {

    public GuiThermalGenerator(ContainerBasicTank<TileEntityThermalGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }

    @Override
    protected void addFluidSlot() {
        addElement(new ThermalGeneratorFluidSlot(this, 58, 41, this.fluidTank, null, 0, 0, true));
    }
}
