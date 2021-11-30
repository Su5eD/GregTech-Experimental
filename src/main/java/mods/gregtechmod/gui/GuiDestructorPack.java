package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.objects.items.containers.ContainerDestructorpack;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiDestructorPack extends GuiIC2<ContainerDestructorpack> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("destructorpack");

    public GuiDestructorPack(ContainerDestructorpack container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawString(8, 6, GtLocale.translateItem("destructorpack.name"), GuiColors.DARK_GRAY, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
