package dev.su5ed.gtexperimental.screen;

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
        addRenderableWidget(IconCycle.createVertical(this.leftPos + 7, this.topPos + 62, 58, () -> this.menu.blockEntity.provideEnergy));
        addRenderableWidget(IconCycle.createVertical(this.leftPos + 25, this.topPos + 62, 76, () -> this.menu.blockEntity.autoOutput));
        addRenderableWidget(IconCycle.createVertical(this.leftPos + 43, this.topPos + 62, 94, () -> this.menu.blockEntity.getMachineController().isStrictInputSides()));
    }

    private double getProgressRatio() {
        return this.menu.blockEntity.recipeHandler.getProgress() / (double) Math.max(this.menu.blockEntity.recipeHandler.getMaxProgress(), 1);
    }

    public static class AutomaticMaceratorScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_macerator");

        public AutomaticMaceratorScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.MACERATING);
        }
    }

    public static class AutomaticExtractorScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_extractor");

        public AutomaticExtractorScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.EXTRACTING);
        }
    }
}
