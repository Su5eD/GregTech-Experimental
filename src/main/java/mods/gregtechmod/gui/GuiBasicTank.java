package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.inventory.slot.CustomFluidSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBasicTank<T extends ContainerBase<? extends IInventory>> extends GuiInventory<T> {
    protected final GtFluidTank fluidTank;

    public GuiBasicTank(T container, GtFluidTank fluidTank) {
        super(container);
        this.fluidTank = fluidTank;
        addFluidSlot();
    }
    
    protected void addFluidSlot() {
        addElement(new CustomFluidSlot(this, 58, 41, this.fluidTank, null, 0, 0, true));
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        drawString(10, 20, "Liquid Amount", 14211290, false);
        drawString(10, 30, String.valueOf(this.fluidTank.getFluidAmount()), 14211290, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/basic_tank.png");
    }
}
