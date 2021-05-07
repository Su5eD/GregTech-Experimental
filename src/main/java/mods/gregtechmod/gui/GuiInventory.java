package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import ic2.core.GuiIC2;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;

public abstract class GuiInventory<T extends ContainerBase<? extends IInventory>> extends GuiIC2<T> {

    public GuiInventory(T container) {
        super(container);
    }
    
    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        drawTitle();
        drawString(8, this.ySize - 96 + 3, I18n.format("container.inventory"), 4210752, false);
    }
    
    protected void drawTitle() {
        String name = I18n.format(this.container.base.getName());
        drawString(8, 4, name, 4210752, false);
    }
}
