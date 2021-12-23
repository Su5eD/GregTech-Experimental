package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricBufferSmall extends GuiElectricBuffer<ContainerElectricBufferSmall> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_buffer_small");
    
    public GuiElectricBufferSmall(ContainerElectricBufferSmall container) {
        super(container);
    }
    
    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
