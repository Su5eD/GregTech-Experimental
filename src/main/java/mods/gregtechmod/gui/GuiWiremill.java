package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiWiremill extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("wiremill");

    public GuiWiremill(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.EXTRUDING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
