package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.inventory.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.machines.container.ContainerQuantumTank;
import net.minecraft.util.ResourceLocation;

public class GuiQuantumTank extends GuiIC2<ContainerQuantumTank> {

    public GuiQuantumTank(ContainerQuantumTank container) {
        super(container, 166);
        addElement(CustomFluidSlot.createFluidSlot(this, 58, 41, container.base.content, null, 0, 0, true));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        bindTexture();
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        drawString(8, 6, Localization.translate(this.container.base.getName()), 3421236, false);
        drawString(10, 20, "Liquid Amount", 14211290, false);
        drawString(10, 30, String.valueOf(this.container.base.content.getFluidAmount()), 14211290, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(GregtechMod.MODID, "textures/gui/basic_tank.png");
    }
}
