package dev.su5ed.gtexperimental.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleSupplier;

public class ProgressBarWidget extends AbstractWidget {
    private final RecipeProgressBar progressBar;
    private final DoubleSupplier supplier;

    public ProgressBarWidget(int x, int y, RecipeProgressBar progressBar, DoubleSupplier supplier) {
        super(x, y, progressBar.width, progressBar.height, Component.empty());
        this.progressBar = progressBar;
        this.supplier = supplier;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.progressBar.texture);
        double progress = Math.min(this.supplier.getAsDouble(), 1);
        double size = progress * this.width;

        blit(poseStack, this.x, this.y, this.progressBar.x, this.progressBar.y, (int) Math.round(size), this.height);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {}
}
