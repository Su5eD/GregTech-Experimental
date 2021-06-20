package mods.gregtechmod.gui;

import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;
import mods.gregtechmod.util.GtUtil;

public class GuiPlasmaGenerator extends GuiFluidGenerator {
    
    public GuiPlasmaGenerator(ContainerBasicTank<TileEntityFluidGenerator> container, GtFluidTank fluidTank) {
        super(container, fluidTank);
    }

    @Override
    protected String getDisplayName() {
        return GtUtil.translateInfo("plasma_amount");
    }
}
