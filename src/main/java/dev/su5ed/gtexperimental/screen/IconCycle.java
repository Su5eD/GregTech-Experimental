package dev.su5ed.gtexperimental.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class IconCycle extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final ResourceLocation texture;
    private final int textureX;
    private final int textureY;
    private final boolean vertical;
    private final IntSupplier valueSupplier;

    public static IconCycle createVertical(int x, int y, int textureX, BooleanSupplier valueGetter) {
        return create(x, y, GtUtil.COMMON_TEXTURE, textureX, 0, 18, true, valueGetter);
    }

    public static IconCycle create(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, boolean vertical, BooleanSupplier valueGetter) {
        return create(x, y, texture, textureX, textureY, step, vertical, () -> valueGetter.getAsBoolean() ? 1 : 0);
    }

    public static IconCycle create(int x, int y, ResourceLocation texture, int textureX, int textureY, Supplier<Enum<?>> valueGetter) {
        return new IconCycle(x, y, texture, textureX, textureY, 18, 18, false, () -> valueGetter.get().ordinal());
    }

    public static IconCycle create(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, boolean vertical, IntSupplier valueGetter) {
        return new IconCycle(x, y, texture, textureX, textureY, step, step, vertical, valueGetter);
    }

    public IconCycle(int x, int y, ResourceLocation texture, int textureX, int textureY, int step, int height, boolean vertical, IntSupplier valueSupplier) {
        this.x = x;
        this.y = y;
        this.width = step;
        this.height = height;
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.vertical = vertical;
        this.valueSupplier = valueSupplier;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture);

        int textureX = this.vertical ? this.textureX : this.textureX + getXStepOffset();
        int textureY = this.vertical ? this.textureY + getXStepOffset() : this.textureY + getYTextureOffset();

        blit(poseStack, this.x, this.y, textureX, textureY, this.width, this.height);
    }

    protected int getXStepOffset() {
        return this.valueSupplier.getAsInt() * this.width;
    }

    protected int getYTextureOffset() {
        return 0;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {}
}
