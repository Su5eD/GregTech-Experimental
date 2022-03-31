package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferLarge;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricBufferLarge extends GuiElectricBuffer<ContainerElectricBufferLarge> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_buffer_large");

    public GuiElectricBufferLarge(ContainerElectricBufferLarge container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
