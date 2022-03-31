package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerChargeOMat;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiChargeOMat extends GuiInventory<ContainerChargeOMat> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("charge_o_mat");

    public GuiChargeOMat(ContainerChargeOMat container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        GuiEnergyStorage.drawChargeBar(this, this.container.base, 77, 60, 141);
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
