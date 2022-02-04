package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerHatchMaintenance;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiHatchMaintenance extends GuiSimple<ContainerHatchMaintenance> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("hatch_maintenance");

    public GuiHatchMaintenance(ContainerHatchMaintenance container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        drawString(8, 4, I18n.format(this.container.base.getName()), GuiColors.DARK_GRAY, false);
        drawString(8, 12, GtLocale.translateTeBlock(this.container.base, "gui_tooltip"), GuiColors.DARK_GRAY, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
