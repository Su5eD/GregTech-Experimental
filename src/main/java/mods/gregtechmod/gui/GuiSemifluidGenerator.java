package mods.gregtechmod.gui;

import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;

public class GuiSemifluidGenerator extends GuiFluidGenerator {
    
    public GuiSemifluidGenerator(ContainerBasicTank<TileEntityFluidGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }
}
