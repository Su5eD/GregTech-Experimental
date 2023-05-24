package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.gui.element.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialSawmill;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiIndustrialSawmill extends GuiStructure<ContainerIndustrialSawmill> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("industrial_sawmill");

    public GuiIndustrialSawmill(ContainerIndustrialSawmill container) {
        super(container);

        addElement(new CustomFluidSlot(this, 33, 54, container.base.fluidTank, GtUtil.COMMON_TEXTURE, 40, 0, true));
        addElement(new LinkedGauge(this, 58, 28, container.base, "progress", GregtechGauge.SAWING));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected void doWhenInvalid() {
        List<String> wrap = fontRenderer.listFormattedStringToWidth(GtLocale.translateInfo("structure_invalid"), 80);
        for (int i = 0; i < wrap.size(); i++) {
            drawString(70, ySize - 109 + i * 8, wrap.get(i), GuiColors.DARK_GRAY, false);
        }
    }
}
