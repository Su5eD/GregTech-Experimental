package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.tileentities.machines.container.ContainerSonictron;
import net.minecraft.util.ResourceLocation;

public class GuiSonictron extends GuiIC2<ContainerSonictron> {

    public GuiSonictron(ContainerSonictron container) {
        super(container, 166);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/sonictron.png");
    }
}
