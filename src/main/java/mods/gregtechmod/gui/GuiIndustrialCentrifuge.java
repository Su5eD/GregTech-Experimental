package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import ic2.core.gui.GuiElement;
import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.slot.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerIndustrialCentrifuge;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiIndustrialCentrifuge extends GuiIC2<ContainerIndustrialCentrifuge> {

    public GuiIndustrialCentrifuge(ContainerIndustrialCentrifuge container) {
        super(container, 166);
        addElement(CustomFluidSlot.createFluidSlot(this, 109, 64, container.base.tank, GregTechMod.COMMON_TEXTURE, 40, 0, false));

        addElement(new LinkedGauge(this, 98, 38, container.base, "progress", GregtechGauge.ARROW_RIGHT));
        addElement(new LinkedGauge(this, 83, 23, container.base, "progress", GregtechGauge.ARROW_UP));
        addElement(new LinkedGauge(this, 68, 38, container.base, "progress", GregtechGauge.ARROW_LEFT));
        addElement(new LinkedGauge(this, 83, 53, container.base, "progress", GregtechGauge.ARROW_DOWN));
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY) {
        for (GuiElement<?> guiElement : this.elements) {
            if (guiElement.isEnabled()) guiElement.drawForeground(mouseX, mouseY);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        GlStateManager.color(1, 1, 1, 1);
        bindTexture();
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String[] name = I18n.format(this.container.base.getName()).split(" ");
        drawString(110, 4, name[0], 4210752, false);
        drawString(110, 12, name[1], 4210752, false);

        for (GuiElement<?> element : this.elements) {
            if (element.isEnabled()) element.drawBackground(mouseX, mouseY);
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/industrial_centrifuge.png");
    }
}
