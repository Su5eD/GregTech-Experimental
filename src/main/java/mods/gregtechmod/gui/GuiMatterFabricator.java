package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMatterFabricator;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiMatterFabricator extends GuiInventory<ContainerMatterFabricator> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("matter_fabricator");

    public GuiMatterFabricator(ContainerMatterFabricator container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        
        drawString(128, 40, (int) this.container.base.getGuiValue("progress") + "%", GuiColors.WHITE, false);
        GuiEnergyStorage.drawChargeBar(this, this.container.base, 88, 60, 160);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
