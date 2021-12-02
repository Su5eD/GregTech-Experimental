package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAutoCompressor extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("auto_compressor");

    public GuiAutoCompressor(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.COMPRESSING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
