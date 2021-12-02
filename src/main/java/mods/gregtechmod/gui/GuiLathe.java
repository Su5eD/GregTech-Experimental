package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiLathe extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("lathe");

    public GuiLathe(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.TURNING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
