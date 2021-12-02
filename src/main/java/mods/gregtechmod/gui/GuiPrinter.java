package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiPrinter extends GuiBasicMachine<ContainerBasicMachine<?>> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("printer");

    public GuiPrinter(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.SMELTING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
