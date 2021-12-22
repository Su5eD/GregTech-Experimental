package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricBufferSmall extends GuiSimple<ContainerElectricBufferSmall> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_buffer_small");
    
    public GuiElectricBufferSmall(ContainerElectricBufferSmall container) {
        super(container);
        
        addCycleButton(0, 58, 0, 76, 18, 7, 62, () -> container.base.outputEnergy ? 1 : 0);
        addCycleButton(2, 130, 0, 148, 18, 25, 62, () -> container.base.redstoneIfFull ? 1 : 0);
        addCycleButton(4, 148, 0, 166, 18, 43, 62, () -> container.base.invertRedstone ? 1 : 0);
    }
    
    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
