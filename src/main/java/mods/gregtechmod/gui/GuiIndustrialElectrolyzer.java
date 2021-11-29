package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.inventory.gui.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialElectrolyzer;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialElectrolyzer extends GuiInventory<ContainerIndustrialElectrolyzer> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("industrial_electrolyzer");

    public GuiIndustrialElectrolyzer(ContainerIndustrialElectrolyzer container) {
        super(container);
        addElement(new CustomFluidSlot(this, 109, 45, container.base.tank, GtUtil.COMMON_TEXTURE, 40, 0, false));

        addElement(new LinkedGauge(this, 73, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
        addElement(new LinkedGauge(this, 83, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
        addElement(new LinkedGauge(this, 93, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
