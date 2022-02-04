package mods.gregtechmod.gui.element;

import ic2.core.GuiIC2;
import ic2.core.gui.GuiElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.function.IntSupplier;

public class IconCycle extends GuiElement<IconCycle> {
    private final ResourceLocation texture;
    private final int textureX;
    private final int textureY;
    protected final int step;
    private final boolean vertical;
    private final IntSupplier valueSupplier;

    public IconCycle(GuiIC2<?> gui, int x, int y, ResourceLocation texture, int textureX, int textureY, int step, int height, boolean vertical, IntSupplier valueSupplier) {
        super(gui, x, y, step, height);

        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.step = step;
        this.vertical = vertical;
        this.valueSupplier = valueSupplier;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);

        int x = this.x + this.gui.getGuiLeft();
        int y = this.y + this.gui.getGuiTop();
        int textureX = this.vertical ? this.textureX : this.textureX + getXStepOffset();
        int textureY = this.vertical ? this.textureY + getXStepOffset() : this.textureY + getYTextureOffset();

        bindTexture(this.texture);
        GlStateManager.color(1, 1, 1, 1);
        this.gui.drawTexturedModalRect(x, y, textureX, textureY, this.step, this.height);
    }

    protected int getXStepOffset() {
        return this.valueSupplier.getAsInt() * this.step;
    }

    protected int getYTextureOffset() {
        return 0;
    }
}
