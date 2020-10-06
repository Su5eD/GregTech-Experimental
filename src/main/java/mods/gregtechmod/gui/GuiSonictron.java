package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.objects.blocks.tileentities.machines.container.ContainerSonictron;
import net.minecraft.util.ResourceLocation;

public class GuiSonictron extends GuiIC2<ContainerSonictron> {

    public GuiSonictron(ContainerSonictron container) {
        super(container, 166);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(GregtechMod.MODID, "textures/gui/sonictron");
    }
}
