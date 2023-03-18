package mods.gregtechmod.gui;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIDSU;

public class GuiIDSU extends GuiEnergyStorage {

    public GuiIDSU(ContainerIDSU container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        GameProfile owner = this.container.base.getOwner();
        String id = owner != null ? String.valueOf(owner.getId().hashCode()) : "unknown";
        drawString("teblock.gregtechmod_idsu.id", 16, id);
    }

    @Override
    protected int getInfoOffsetY() {
        return 24;
    }
}
