package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricTranslocatorAdvanced;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricTranslocatorAdvanced extends GuiElectricTranslocator<ContainerElectricTranslocatorAdvanced> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_translocator_advanced");

    public GuiElectricTranslocatorAdvanced(ContainerElectricTranslocatorAdvanced container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        drawString(7, 7, this.container.base.inputSide.name(), GuiColors.WHITE, false);
        drawString(138, 7, this.container.base.outputSide.name(), GuiColors.WHITE, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
