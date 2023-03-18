package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;

public class GuiLESU extends GuiEnergyStorage {

    public GuiLESU(ContainerEnergyStorage container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        if (this.container.base.getEUCapacity() >= 1999999999) {
            drawString("teblock.gregtechmod_lesu.warning", 48);
            drawString("teblock.gregtechmod_lesu.max_reached", 56);
        }
    }
}
