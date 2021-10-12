package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerDistillationTower;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiDistillationTower extends GuiStructure<ContainerDistillationTower> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("distillation_tower");

    public GuiDistillationTower(ContainerDistillationTower container) {
        super(container);
        
        addElement(new LinkedGauge(this, 80, 4, container.base, "progress", GregtechGauge.DISTILLING));
    }

    @Override
    protected void drawTitle() {
        String[] name = I18n.format(this.container.base.getName()).split(" ");
        drawString(116, 4, name[0], GuiColors.DARK_GRAY, false);
        drawString(116, 12, name[1], GuiColors.DARK_GRAY, false);
    }

    @Override
    protected void doWhenInvalid() {
        List<String> wrap = fontRenderer.listFormattedStringToWidth(GtUtil.translateInfo("structure_invalid"), 55);
        for (int i = 0; i < wrap.size(); i++) {
            drawString(116, 20 + i * 8, wrap.get(i), GuiColors.DARK_GRAY, false);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
