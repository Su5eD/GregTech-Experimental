package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFusionReactor;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiFusionReactor extends GuiSimple<ContainerFusionReactor> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("fusion_reactor");

    public GuiFusionReactor(ContainerFusionReactor container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        GuiEnergyStorage.drawChargeBar(this, this.container.base, 5, 156, 78, 147);
    }
}
