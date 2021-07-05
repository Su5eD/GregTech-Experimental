package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerHatchMaintenance;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiHatchMaintenance extends GuiIC2<ContainerHatchMaintenance> {

    public GuiHatchMaintenance(ContainerHatchMaintenance container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        drawString(8, 4, I18n.format(this.container.base.getName()), 4210752, false);
        drawString(8, 12, GtUtil.translate("teblock.hatch_maintenance.gui_tooltip"), 4210752, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/hatch_maintenance.png");
    }
}
