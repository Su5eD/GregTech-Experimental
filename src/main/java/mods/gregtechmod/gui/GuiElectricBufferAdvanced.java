package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferAdvanced;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricBufferAdvanced extends GuiElectricBuffer<ContainerElectricBufferAdvanced> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_buffer_advanced");

    public GuiElectricBufferAdvanced(ContainerElectricBufferAdvanced container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        drawString(100, 65, Integer.toString(this.container.base.targetSlot), GuiColors.WHITE, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
