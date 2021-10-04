package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialSawmill;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialSawmill extends GuiStructure<ContainerIndustrialSawmill> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("industrial_sawmill");

    public GuiIndustrialSawmill(ContainerIndustrialSawmill container) {
        super(container);
        
        addElement(new LinkedGauge(this, 58, 28, container.base, "progress", GregtechGauge.SAWING));
        addElement(new LinkedGauge(this, 33, 33, container.base, "water_level", GregtechGauge.WATER_LEVEL));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
