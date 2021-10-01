package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerChargeOMat;
import net.minecraft.util.ResourceLocation;

public class GuiChargeOMat extends GuiInventory<ContainerChargeOMat> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/charge_o_mat.png");

    public GuiChargeOMat(ContainerChargeOMat container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        
        double charge = this.container.base.getStoredEU() / (double) this.container.base.getEUCapacity();
        GuiEnergyStorage.drawChargeBar(this, 77, 60, 141, charge);
    }

    @Override
    protected void drawTitle() {
        // noop
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
