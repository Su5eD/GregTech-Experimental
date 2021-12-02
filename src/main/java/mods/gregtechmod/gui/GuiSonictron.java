package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerSonictron;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiSonictron extends GuiIC2<ContainerSonictron> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("sonictron");

    public GuiSonictron(ContainerSonictron container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
