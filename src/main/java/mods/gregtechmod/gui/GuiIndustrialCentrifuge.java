package mods.gregtechmod.gui;

import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.gui.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialCentrifuge;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialCentrifuge extends GuiInventory<ContainerIndustrialCentrifuge> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("industrial_centrifuge");

    public GuiIndustrialCentrifuge(ContainerIndustrialCentrifuge container) {
        super(container);
        addElement(new CustomFluidSlot(this, 109, 64, container.base.tank, GregTechMod.COMMON_TEXTURE, 40, 0, false));

        addElement(new LinkedGauge(this, 98, 38, container.base, "progress", GregtechGauge.SMALL_ARROW_RIGHT));
        addElement(new LinkedGauge(this, 83, 23, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
        addElement(new LinkedGauge(this, 68, 38, container.base, "progress", GregtechGauge.SMALL_ARROW_LEFT));
        addElement(new LinkedGauge(this, 83, 53, container.base, "progress", GregtechGauge.SMALL_ARROW_DOWN));
    }

    @Override
    protected void drawTitle() {
        String[] name = I18n.format(this.container.base.getName()).split(" ");
        drawString(110, 4, name[0], 4210752, false);
        drawString(110, 12, name[1], 4210752, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
