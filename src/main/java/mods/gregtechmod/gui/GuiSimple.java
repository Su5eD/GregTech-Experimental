package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import ic2.core.GuiIC2;
import ic2.core.gui.CustomButton;
import ic2.core.gui.CycleHandler;
import ic2.core.gui.INumericValueHandler;
import mods.gregtechmod.gui.element.IconCycle;
import mods.gregtechmod.util.ButtonStateHandler;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.function.IntSupplier;

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
    
    // TODO Migrate to icon cycle
    protected void addVerticalCycleButton(int id, int uS, int vS, int uE, int vE, int x, int y, IntSupplier valueGetter) {
        addVerticalCycleButton(id, uS, vS, uE, vE, x, y, 18, GtUtil.COMMON_TEXTURE, valueGetter);
    }
    
    protected void addVerticalCycleButton(int id, int uS, int vS, int uE, int vE, int x, int y, int step, ResourceLocation texture, IntSupplier valueGetter) {
        addCycleButton(id, uS, vS, uE, vE, x, y, step, true, 2, true, texture, valueGetter);
    }
    
    protected void addCycleButton(int id, int uS, int vS, int uE, int vE, int x, int y, int step, boolean vertical, int options, boolean sendUpdate, ResourceLocation texture, IntSupplier valueGetter) {
        INumericValueHandler valueHandler = new ButtonStateHandler((TileEntity) this.container.base, id, sendUpdate, valueGetter);
        CycleHandler cycleHandler = new CycleHandler(uS, vS, uE, vE, step, vertical, options, valueHandler);
        addElement(new CustomButton(this, x, y, step, step, cycleHandler, texture, cycleHandler));
    }
    
    protected void addIconCycle(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, boolean vertical, IntSupplier valueGetter) {
        addElement(new IconCycle(this, x, y, texture, textureX, textureY, step, vertical, valueGetter));
    }
}
