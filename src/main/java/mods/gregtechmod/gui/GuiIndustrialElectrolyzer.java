package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import ic2.core.gui.LinkedGauge;
import ic2.core.init.Localization;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.slot.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerIndustrialElectrolyzer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialElectrolyzer extends GuiIC2<ContainerIndustrialElectrolyzer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/industrial_electrolyzer.png");

    public GuiIndustrialElectrolyzer(ContainerIndustrialElectrolyzer container) {
        super(container);
        addElement(CustomFluidSlot.createFluidSlot(this, 109, 45, container.base.tank, GregTechMod.COMMON_TEXTURE, 40, 0, false));

        addElement(new LinkedGauge(this, 73, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
        addElement(new LinkedGauge(this, 83, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
        addElement(new LinkedGauge(this, 93, 34, container.base, "progress", GregtechGauge.SMALL_ARROW_UP));
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        drawString(8, 4, name, 4210752, false);
        drawString(8, this.ySize - 96 + 3, I18n.format("container.inventory"), 4210752, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
