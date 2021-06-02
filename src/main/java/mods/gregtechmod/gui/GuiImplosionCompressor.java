package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerImplosionCompressor;
import net.minecraft.util.ResourceLocation;

public class GuiImplosionCompressor extends GuiStructure<ContainerImplosionCompressor> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/implosion_compressor.png");

    public GuiImplosionCompressor(ContainerImplosionCompressor container) {
        super(container);
        
        addElement(new LinkedGauge(this, 58, 28, container.base, "progress", GregtechGauge.IMPLODING));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
