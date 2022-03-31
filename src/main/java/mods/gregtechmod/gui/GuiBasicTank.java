package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import mods.gregtechmod.gui.element.CustomFluidSlot;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBasicTank<T extends ContainerBase<? extends IInventory>> extends GuiInventory<T> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("basic_tank");
    protected final GtFluidTank fluidTank;

    public GuiBasicTank(T container, GtFluidTank fluidTank) {
        super(container);
        this.fluidTank = fluidTank;
        addFluidSlot();
    }

    protected void addFluidSlot() {
        addElement(new CustomFluidSlot(this, 58, 41, this.fluidTank, false));
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        drawString(10, 20, getDisplayName(), GuiColors.LIGHT_GRAY, false);
        drawString(10, 30, JavaUtil.formatNumber(this.fluidTank.getFluidAmount()), GuiColors.LIGHT_GRAY, false);
    }

    protected String getDisplayName() {
        return GtLocale.translateInfo("liquid_amount");
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
