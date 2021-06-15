package mods.gregtechmod.gui;

import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;

public class GuiDieselGenerator extends GuiFluidGenerator {
    
    public GuiDieselGenerator(ContainerBasicTank<TileEntityFluidGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }
}
