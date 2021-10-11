package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAdvancedSafe;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedSafe extends GuiIC2<ContainerAdvancedSafe> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("advanced_safe");

    public GuiAdvancedSafe(ContainerAdvancedSafe container) {
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
