package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiPrinter extends GuiBasicMachine<ContainerBasicMachine<TileEntityBasicMachine<?, ?, ?, ?>>> {

    public GuiPrinter(ContainerBasicMachine<TileEntityBasicMachine<?, ?, ?, ?>> container) {
        super(new ResourceLocation(Reference.MODID, "textures/gui/printer.png"), container, GregtechGauge.SMELTING);
    }
}
