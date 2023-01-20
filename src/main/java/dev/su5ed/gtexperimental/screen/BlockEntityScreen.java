package dev.su5ed.gtexperimental.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.su5ed.gtexperimental.network.GregTechNetwork;
import dev.su5ed.gtexperimental.network.SlotScrollPacket;
import dev.su5ed.gtexperimental.util.inventory.InteractiveSlot;
import dev.su5ed.gtexperimental.util.inventory.ScrollDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public abstract class BlockEntityScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation background;

    public BlockEntityScreen(T menu, Inventory playerInventory, Component title, ResourceLocation background) {
        super(menu, playerInventory, title);

        this.background = background;
        this.height = this.imageHeight = 217;
    }

    @Override
    protected void init() {
        super.init();

        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        Slot slot = findSlot(mouseX, mouseY);
        if (slot instanceof InteractiveSlot interactiveSlot) {
            ScrollDirection direction = delta > 0 ? ScrollDirection.UP : ScrollDirection.DOWN;

            boolean shift = Screen.hasShiftDown();
            SlotScrollPacket packet = new SlotScrollPacket(this.menu.containerId, slot.index, this.menu.getStateId(), direction, shift);
            GregTechNetwork.INSTANCE.sendToServer(packet);

            interactiveSlot.slotScroll(this.minecraft.player, direction, shift);
        }
        
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public final void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderFg(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, this.background);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        blit(poseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, ScreenColors.DARK_GREY);
    }

    public void renderFg(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {}
}

