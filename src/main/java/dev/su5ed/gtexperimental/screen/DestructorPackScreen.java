package dev.su5ed.gtexperimental.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.su5ed.gtexperimental.menu.DestructorPackMenu;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DestructorPackScreen extends AbstractContainerScreen<DestructorPackMenu> {
    public static final ResourceLocation BACKGROUND = GtUtil.guiTexture("destructorpack");

    public DestructorPackScreen(DestructorPackMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        blit(poseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
