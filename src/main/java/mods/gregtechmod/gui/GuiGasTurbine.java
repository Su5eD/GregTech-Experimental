package mods.gregtechmod.gui;

import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;
import mods.gregtechmod.util.GtLocale;

public class GuiGasTurbine extends GuiFluidGenerator {

    public GuiGasTurbine(ContainerBasicTank<TileEntityFluidGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }

    @Override
    protected String getDisplayName() {
        return GtLocale.translateInfo("gas_amount");
    }
}
