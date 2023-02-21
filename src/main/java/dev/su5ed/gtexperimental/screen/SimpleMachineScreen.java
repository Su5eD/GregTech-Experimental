package dev.su5ed.gtexperimental.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.function.Function;

public class SimpleMachineScreen extends BlockEntityScreen<SimpleMachineMenu> {
    public static final Function<String, ResourceLocation> BACKGROUNDS = Util.memoize(GtUtil::guiTexture);

    private final RecipeProgressBar progressBar;

    public static SimpleMachineScreen autoMacerator(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
        return new SimpleMachineScreen(menu, playerInventory, title, background("auto_macerator"), RecipeProgressBar.MACERATING);
    }

    public static ResourceLocation background(String name) {
        return BACKGROUNDS.apply(name);
    }

    public SimpleMachineScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title, ResourceLocation background, RecipeProgressBar progressBar) {
        super(menu, playerInventory, title, background);

        this.progressBar = progressBar;
    }

    @Override
    protected void init() {
        super.init();
        
        addRenderableWidget(new ProgressBarWidget(this.leftPos + 78, this.topPos + 24, this.progressBar, this::getProgressRatio));
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);

        this.font.draw(poseStack, this.menu.blockEntity.recipeHandler.getProgress() + " / " + this.menu.blockEntity.recipeHandler.getMaxProgress(), this.titleLabelX, 50, ScreenColors.DARK_GREY);
    }
    
    private double getProgressRatio() {
        return this.menu.blockEntity.recipeHandler.getProgress() / (double) Math.max(this.menu.blockEntity.recipeHandler.getMaxProgress(), 1);
    }
}
