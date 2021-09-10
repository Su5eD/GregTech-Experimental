package mods.gregtechmod.gui;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;

public class GuiIDSU extends GuiEnergyStorage {

    public GuiIDSU(ContainerEnergyStorage container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        GameProfile owner = this.container.base.getOwner();
        String id = owner != null ? String.valueOf(owner.getId().hashCode()) : "unknown";
        drawString("teblock.idsu.id", 16, id);
    }

    @Override
    protected int getInfoOffsetY() {
        return 24;
    }
}
