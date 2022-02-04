package mods.gregtechmod.gui.element;

import ic2.core.GuiIC2;
import net.minecraft.util.ResourceLocation;

import java.util.function.IntSupplier;

public class IconCycleGrid extends IconCycle {
    private final IntSupplier yValueSupplier;

    public IconCycleGrid(GuiIC2<?> gui, int x, int y, ResourceLocation texture, int textureX, int textureY, int step, int height, boolean vertical, IntSupplier xValueSupplier, IntSupplier yValueSupplier) {
        super(gui, x, y, texture, textureX, textureY, step, height, vertical, xValueSupplier);

        this.yValueSupplier = yValueSupplier;
    }

    @Override
    protected int getYTextureOffset() {
        return this.yValueSupplier.getAsInt() * this.step;
    }
}
