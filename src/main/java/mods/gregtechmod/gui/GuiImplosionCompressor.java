package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerImplosionCompressor;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiImplosionCompressor extends GuiStructure<ContainerImplosionCompressor> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("implosion_compressor");

    public GuiImplosionCompressor(ContainerImplosionCompressor container) {
        super(container);

        addElement(new LinkedGauge(this, 58, 28, container.base, "progress", GregtechGauge.IMPLODING));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
