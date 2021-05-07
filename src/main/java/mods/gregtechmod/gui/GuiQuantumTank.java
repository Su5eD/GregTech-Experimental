package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.inventory.slot.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerQuantumTank;
import net.minecraft.util.ResourceLocation;

public class GuiQuantumTank extends GuiInventory<ContainerQuantumTank> {

    public GuiQuantumTank(ContainerQuantumTank container) {
        super(container);
        addElement(CustomFluidSlot.createFluidSlot(this, 58, 41, container.base.content, null, 0, 0, true));
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        drawString(10, 20, "Liquid Amount", 14211290, false);
        drawString(10, 30, String.valueOf(this.container.base.content.getFluidAmount()), 14211290, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/basic_tank.png");
    }
}
