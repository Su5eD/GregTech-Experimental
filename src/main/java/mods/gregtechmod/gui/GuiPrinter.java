package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiPrinter extends GuiBasicMachine<ContainerBasicMachine<?>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/printer.png");

    public GuiPrinter(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.SMELTING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
