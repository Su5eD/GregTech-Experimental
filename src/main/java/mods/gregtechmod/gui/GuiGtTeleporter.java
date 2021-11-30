package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerGtTeleporter;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiGtTeleporter extends GuiIC2<ContainerGtTeleporter> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("gt_teleporter");

    public GuiGtTeleporter(ContainerGtTeleporter container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        drawString(46, 8, I18n.format(this.container.base.getName()), GuiColors.WHITE, false);
        drawString(46, 16, "X: " + this.container.base.targetPos.getX(), GuiColors.WHITE, false);
        drawString(46, 24, "Y: " + this.container.base.targetPos.getY(), GuiColors.WHITE, false);
        drawString(46, 32, "Z: " + this.container.base.targetPos.getZ(), GuiColors.WHITE, false);
        
        if (this.container.base.canTeleportAcrossDimensions()) {
            drawString(46, 40, GtLocale.translateTeBlock("gt_teleporter", "dimension_short", this.container.base.targetDimension.getName()), GuiColors.WHITE, false);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
