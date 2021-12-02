package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBlastFurnace;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialBlastFurnace extends GuiStructure<ContainerBlastFurnace> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("industrial_blast_furnace");

    public GuiIndustrialBlastFurnace(ContainerBlastFurnace container) {
        super(container);
        
        addElement(new LinkedGauge(this, 58, 28, container.base, "progress", GregtechGauge.BLASTING));
    }
        
    @Override
    protected void doWhenValid() {
        drawString(8, ySize - 103, GtLocale.translateInfo("heat_capacity", this.container.base.getHeatCapacity()), GuiColors.DARK_GRAY, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
