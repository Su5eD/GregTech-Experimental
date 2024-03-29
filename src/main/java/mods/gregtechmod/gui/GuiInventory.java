package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;

public abstract class GuiInventory<T extends ContainerBase<? extends IInventory>> extends GuiSimple<T> {

    public GuiInventory(T container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        drawTitle();
        drawString(8, this.ySize - 96 + 3, I18n.format("container.inventory"), GuiColors.DARK_GRAY, false);
    }

    protected void drawTitle() {
        String name = I18n.format(this.container.base.getName());
        drawString(8, 4, name, GuiColors.DARK_GRAY, false);
    }
}
