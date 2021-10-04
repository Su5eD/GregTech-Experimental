package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialGrinder;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialGrinder extends GuiStructure<ContainerIndustrialGrinder> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("industrial_grinder");

    public GuiIndustrialGrinder(ContainerIndustrialGrinder container) {
        super(container);
        
        addElement(new LinkedGauge(this, 58, 24, container.base, "progress", GregtechGauge.MACERATING));
        addElement(new LinkedGauge(this, 33, 33, container.base, "water_level", GregtechGauge.WATER_LEVEL));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
