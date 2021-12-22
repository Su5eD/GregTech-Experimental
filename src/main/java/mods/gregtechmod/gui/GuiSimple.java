package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import ic2.core.GuiIC2;
import ic2.core.gui.CustomButton;
import ic2.core.gui.CycleHandler;
import mods.gregtechmod.util.ButtonStateHandler;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

public abstract class GuiSimple<T extends ContainerBase<? extends IInventory>> extends GuiIC2<T> {

    public GuiSimple(T container) {
        super(container);
    }

    public GuiSimple(T container, int ySize) {
        super(container, ySize);
    }

    public GuiSimple(T container, int xSize, int ySize) {
        super(container, xSize, ySize);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    protected void addCycleButton(int id, int uS, int vS, int uE, int vE, int x, int y, Supplier<Integer> valueGetter) {
        CycleHandler cycleHandler = new CycleHandler(uS, vS, uE, vE, 18, true, 2,
                new ButtonStateHandler((TileEntity) this.container.base, id, valueGetter));
        this.addElement(new CustomButton(this, x, y, 18, 18, cycleHandler, GtUtil.COMMON_TEXTURE, cycleHandler));
    }
}
