package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import ic2.core.GuiIC2;
import mods.gregtechmod.gui.element.IconCycle;
import mods.gregtechmod.gui.element.IconCycleGrid;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
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

    protected void addVerticalIconCycle(int x, int y, int textureX, BooleanSupplier valueGetter) {
        addIconCycle(x, y, GtUtil.COMMON_TEXTURE, textureX, 0, 18, true, valueGetter);
    }

    protected void addIconCycle(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, boolean vertical, BooleanSupplier valueGetter) {
        addIconCycle(x, y, texture, textureX, textureY, step, vertical, () -> valueGetter.getAsBoolean() ? 1 : 0);
    }

    protected void addIconCycle(int x, int y, ResourceLocation texture, int textureX, int textureY, Supplier<Enum<?>> valueGetter) {
        addIconCycle(x, y, texture, textureX, textureY, 18, 18, false, () -> valueGetter.get().ordinal());
    }

    protected void addIconCycle(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, boolean vertical, IntSupplier valueGetter) {
        addIconCycle(x, y, texture, textureX, textureY, step, step, vertical, valueGetter);
    }

    protected void addIconCycle(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, int height, boolean vertical, IntSupplier valueGetter) {
        addElement(new IconCycle(this, x, y, texture, textureX, textureY, step, height, vertical, valueGetter));
    }

    protected void addIconCycleGrid(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, boolean vertical, IntSupplier xValueGetter, BooleanSupplier yValueGetter) {
        addElement(new IconCycleGrid(this, x, y, texture, textureX, textureY, step, step, vertical, xValueGetter, () -> yValueGetter.getAsBoolean() ? 1 : 0));
    }
}
